package eu.execom.hawaii.service;

import eu.execom.hawaii.dto.CreateTokenDto;
import eu.execom.hawaii.model.Allowance;
import eu.execom.hawaii.model.LeaveProfile;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.UserPushToken;
import eu.execom.hawaii.model.Year;
import eu.execom.hawaii.model.enumerations.UserStatusType;
import eu.execom.hawaii.repository.LeaveProfileRepository;
import eu.execom.hawaii.repository.UserPushTokensRepository;
import eu.execom.hawaii.repository.UserRepository;
import eu.execom.hawaii.repository.YearRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

/**
 * User management service.
 */
@Slf4j
@Service
public class UserService {

  private static final int HALF_DAY = 4;
  private static final int FULL_DAY = 8;
  private static final int FIVE_YEARS = 5;
  private static final int TEN_YEARS = 10;
  private static final int FIFTEEN_YEARS = 15;
  private static final MonthDay FEBRUARY_TWENTY_NINE = MonthDay.of(2, 29);

  private UserRepository userRepository;
  private LeaveProfileRepository leaveProfileRepository;
  private UserPushTokensRepository userPushTokensRepository;
  private YearRepository yearRepository;
  private EmailService emailService;

  @Autowired
  public UserService(UserRepository userRepository, LeaveProfileRepository leaveProfileRepository,
      UserPushTokensRepository userPushTokensRepository, YearRepository yearRepository, EmailService emailService) {
    this.userRepository = userRepository;
    this.leaveProfileRepository = leaveProfileRepository;
    this.userPushTokensRepository = userPushTokensRepository;
    this.yearRepository = yearRepository;
    this.emailService = emailService;
  }

  /**
   * Retrieves a list of all users from repository.
   *
   * @return a list of all users, both active and non-active
   */
  public List<User> findAllUsers() {
    return userRepository.findAll();
  }

  /**
   * Retrieves a list of all users from repository.
   *
   * @param userStatusType what is user status (ACTIVE, INACTIVE or DELETED)
   * @return a list of all users.
   */
  public List<User> findAllByUserStatusType(List<UserStatusType> userStatusType) {
    return userRepository.findAllByUserStatusTypeIn(userStatusType);
  }

  /**
   * Retrieves a list of all users searched by given query.
   *
   * @param userStatusType what is user status (ACTIVE, INACTIVE or DELETED)
   * @param searchQuery    search by given query.
   * @param pageable       the Pageable information about size per page and number of page.
   * @return a list of queried users by given search.
   */
  public Page<User> findAllByActiveAndEmailOrFullName(UserStatusType userStatusType, String searchQuery,
      Pageable pageable) {
    return userRepository.findAllByUserStatusTypeAndEmailContainingOrFullNameContaining(userStatusType, searchQuery,
        searchQuery, pageable);
  }

  /**
   * Retrieves a User with a specific id
   *
   * @param id User email
   * @return User with specified email if exists
   */
  @Transactional(readOnly = true)
  public User getUserById(Long id) {
    return userRepository.getOne(id);
  }

  /**
   * Retrieves a User with a specific email.
   *
   * @param email User email
   * @return User with specified email if exists
   * @throws EntityNotFoundException if a user with given email is not found
   */
  @Transactional(readOnly = true)
  public User findByEmail(String email) {
    return userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
  }

  /**
   * Saves the provided User to repository.
   *
   * @param user the User entity to be persisted.
   */
  public User save(User user) {
    return userRepository.save(user);
  }

  /**
   * Sets user to be active. When user is created he is initially inactive.
   * Only inactive users can become active.
   * Once user is deleted, he can only turn back to being inactive, and then active.
   */
  public void activate(Long id) {
    var user = userRepository.getOne(id);

    UserStatusType userStatusType = user.getUserStatusType();

    switch (userStatusType) {
      case INACTIVE:
        user.setUserStatusType(UserStatusType.ACTIVE);
        break;
      case DELETED:
        user.setUserStatusType(UserStatusType.INACTIVE);
        break;
      case ACTIVE:
        log.warn("User already active");
        break;
      default:
        throw new IllegalArgumentException("Unsupported user status type: " + userStatusType);
    }
    userRepository.save(user);
  }

  /**
   * Logically deletes User.
   */
  @Transactional
  public void delete(Long userId) {
    var user = userRepository.getOne(userId);
    user.setUserStatusType(UserStatusType.DELETED);
    userRepository.save(user);
  }

  /**
   * Gets users leave profile and currently active years, and creates allowances
   * according with values from leave profile
   */
  public User createAllowanceForUserOnCreateUser(User user) {
    var leaveProfile = leaveProfileRepository.getOne(user.getLeaveProfile().getId());
    var openedActiveYears = yearRepository.findAllByYearGreaterThanEqual(LocalDate.now().getYear());
    var userAllowances = user.getAllowances();
    for (Year year : openedActiveYears) {
      Allowance allowance = createAllowance(user, year, leaveProfile);
      userAllowances.add(allowance);
    }
    return save(user);
  }

  /**
   * If year when user started working at execom is not the same as current year that means
   * that next year is open, and allowances are created solely based on  users leave profile.
   *
   * If year is same as current than this is the year when user started working at execom and
   * allowances are created based on remaining days in this year in reference to users leave profile.
   *
   * Allowance is rounded to 4h witch is half a day, because that's the smallest period of time that can be
   * taken as annual leave.
   */
  private Allowance createAllowance(User user, Year year, LeaveProfile leaveProfile) {
    Allowance allowance = new Allowance();
    allowance.setUser(user);
    allowance.setYear(year);

    if (user.getStartedWorkingAtExecomDate().getYear() != year.getYear()) {
      allowance.setAnnual(leaveProfile.getEntitlement());
      allowance.setTraining(leaveProfile.getTraining());
    } else {
      LocalDate lastDayOfYear = user.getStartedWorkingAtExecomDate().with(lastDayOfYear());
      int daysInYear = java.time.Year.of(year.getYear()).length();
      long daysInYearRemaining = ChronoUnit.DAYS.between(user.getStartedWorkingAtExecomDate(), lastDayOfYear);

      float annualAllowancePerDay = (float) leaveProfile.getEntitlement() / (daysInYear * HALF_DAY);
      float annualTrainingPerDay = (float) leaveProfile.getTraining() / (daysInYear * HALF_DAY);

      allowance.setAnnual(Math.round(daysInYearRemaining * annualAllowancePerDay) * HALF_DAY);
      allowance.setTraining(Math.round(daysInYearRemaining * annualTrainingPerDay) * HALF_DAY);
    }

    return allowance;
  }

  /**
   * Updates values for allowances for active years. Since Leave Profile was just updated, values
   * for already created allowances for currently active years need to be updated as well.
   */
  private void updateAllowanceForUserOnLeaveProfileUpdate(User user) {
    var openedActiveYears = yearRepository.findAllByYearGreaterThanEqual(LocalDate.now().getYear());
    var userAllowances = user.getAllowances();
    for (Year year : openedActiveYears) {
      userAllowances.stream()
                    .filter(allowance1 -> allowance1.getYear().equals(year))
                    .forEach(allowance1 -> allowance1.setAnnual(allowance1.getAnnual() + FULL_DAY));
    }
    userRepository.save(user);
  }

  /**
   * Check every day at 6:00 am to see if years of service needs to be incremented.
   * If user reaches 5, 10, 15 years... Update leave profile, save, send update mail.
   */
  @Scheduled(cron = "0 0 6 * * *")
  public void addServiceYearsToUser() {
    List<User> users = userRepository.findAllByUserStatusTypeIn(Collections.singletonList(UserStatusType.ACTIVE));

    Supplier<Stream<User>> userStream = () -> users.stream()
                                                   .filter(user -> startedWorkingToday(
                                                       MonthDay.from(user.getStartedWorkingDate())));

    userStream.get()
              .forEach(user -> {
                          user.setYearsOfService(user.getYearsOfService() + 1);
                          userRepository.save(user);
    });

    userStream.get()
              .filter(shouldUpdateLeaveProfile())
              .forEach(user -> {
                          user.getLeaveProfile().setId(user.getLeaveProfile().getId() + 1);
                          emailService.createLeaveProfileUpdateEmailAndSendForApproval(user);
                          updateAllowanceForUserOnLeaveProfileUpdate(user);
    });
  }

  private boolean startedWorkingToday(MonthDay startedWorkingDate) {
    if ((startedWorkingDate == FEBRUARY_TWENTY_NINE) && !LocalDate.now().isLeapYear()) {
      return MonthDay.of(2, 28).equals(MonthDay.from(LocalDate.now()));
    } else {
      return startedWorkingDate.equals(MonthDay.from(LocalDate.now()));
    }
  }

  private Predicate<User> shouldUpdateLeaveProfile() {
    return user -> user.getYearsOfService() == FIVE_YEARS ||
                   user.getYearsOfService() == TEN_YEARS  ||
                   user.getYearsOfService() == FIFTEEN_YEARS;
  }

  /**
   * First, checks if the exact same push token for authenticated user already exists
   * If it doesn't exist, create new UserPushToken and return the new UserPushToken
   * If already exists, just pass him through and return the old UserPushToken
   */
  @Transactional
  public UserPushToken createUserToken(User authUser, CreateTokenDto createTokenDto) {
    UserPushToken authUserPushToken = userPushTokensRepository.findOneByUserAndPushToken(authUser,
        createTokenDto.getPushToken());

    if (authUserPushToken == null) {
      UserPushToken userPushToken = new UserPushToken();
      userPushToken.setUser(authUser);
      userPushToken.setPushToken(createTokenDto.getPushToken());
      userPushToken.setPlatform(createTokenDto.getPlatform());
      userPushToken.setName(createTokenDto.getName());
      userPushToken.setCreateDateTime(LocalDateTime.now());
      userPushTokensRepository.save(userPushToken);
      return userPushToken;
    } else {
      return userPushTokensRepository.findOneByPushToken(createTokenDto.getPushToken());
    }
  }

  @Transactional
  public void deleteUserPushToken(User authUser, Long id) {
    List<UserPushToken> userPushTokens = authUser.getUserPushTokens();
    for (UserPushToken userPushToken : userPushTokens) {
      if (userPushToken.getId().equals(id)) {
        userPushTokensRepository.delete(userPushToken);
      }
    }
  }
}