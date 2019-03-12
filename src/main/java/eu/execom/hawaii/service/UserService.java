package eu.execom.hawaii.service;

import eu.execom.hawaii.dto.CreateTokenDto;
import eu.execom.hawaii.model.Allowance;
import eu.execom.hawaii.model.LeaveProfile;
import eu.execom.hawaii.model.Team;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.UserPushToken;
import eu.execom.hawaii.model.Year;
import eu.execom.hawaii.model.audit.UserAudit;
import eu.execom.hawaii.model.enumerations.OperationPerformed;
import eu.execom.hawaii.model.enumerations.UserStatusType;
import eu.execom.hawaii.repository.LeaveProfileRepository;
import eu.execom.hawaii.repository.UserPushTokensRepository;
import eu.execom.hawaii.repository.UserRepository;
import eu.execom.hawaii.repository.YearRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

/**
 * User management service.
 */
@Slf4j
@Service
public class UserService {

  private static final int HALF_DAY = 4;
  private static final MonthDay HALF_YEAR_DATE = MonthDay.of(6, 30);
  private static final int MINIMUM_QUERY_SIZE = 3;

  private UserRepository userRepository;
  private LeaveProfileRepository leaveProfileRepository;
  private UserPushTokensRepository userPushTokensRepository;
  private YearRepository yearRepository;
  private AuditInformationService auditInformationService;

  @Autowired
  public UserService(UserRepository userRepository, LeaveProfileRepository leaveProfileRepository,
      UserPushTokensRepository userPushTokensRepository, YearRepository yearRepository,
      AuditInformationService auditInformationService) {
    this.userRepository = userRepository;
    this.leaveProfileRepository = leaveProfileRepository;
    this.userPushTokensRepository = userPushTokensRepository;
    this.yearRepository = yearRepository;
    this.auditInformationService = auditInformationService;
  }

  /**
   * Retrieves a list of all users from repository.
   *
   * @param pageable the Pageable information about size per page and number of page.
   * @return a list of all users, both active and non-active
   */
  public Page<User> findAll(Pageable pageable) {
    return userRepository.findAll(pageable);
  }

  /**
   * Retrieves a list of all active users from repository with a matching team.
   *
   * @param team Team a user belongs to
   * @return a list of all active users belonging to a given team
   */
  public List<User> findAllActiveUsersByTeam(Team team) {
    return userRepository.findAllByUserStatusTypeAndTeam(UserStatusType.ACTIVE, team);
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
   * Retrieves a list of all users from repository(Pageable)
   *
   * @param userStatusType what is user status (ACTIVE, INACTIVE or DELETED)
   * @param pageable the Pageable information about size per page and number of page.
   * @return list of all users from repository with given status (pageable).
   */
  public Page<User> findAllByUserStatusTypePage(List<UserStatusType> userStatusType, Pageable pageable) {
    return userRepository.findAllByUserStatusType(userStatusType, pageable);
  }

  /**
   * Retrieves a list of all users searched by given query.
   *
   * @param userStatusType what is user status (ACTIVE, INACTIVE or DELETED)
   * @param searchQuery search by given query.
   * @param pageable the Pageable information about size per page and number of page.
   * @return a list of queried users by given search.
   */
  public Page<User> findAllByActiveAndEmailOrFullName(UserStatusType userStatusType, String searchQuery,
      Pageable pageable) {
    if (searchQuery.length() <= MINIMUM_QUERY_SIZE) {
      return Page.empty();
    }
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
    return userRepository.findByEmail(email)
                         .orElseThrow(() -> new EntityNotFoundException("User with given email doesn't exist."));
  }

  /**
   * Retrieves a list of Users with a given fullname containing.
   *
   * @param fullNameQuery User fullname query
   * @return List of users with given fullname containing.
   */
  @Transactional
  public List<User> findByFullNameContaining(String fullNameQuery) {
    if (fullNameQuery.length() <= MINIMUM_QUERY_SIZE) {
      return Collections.emptyList();
    }
    return userRepository.findAllByFullNameContaining(fullNameQuery);
  }

  /**
   * Saves the provided User to repository.
   *
   * @param user the User entity to be persisted.
   * @return saved User.
   */
  @Transactional
  public User save(User user) {
    return userRepository.save(user);
  }

  /**
   * Saves the provided User to repository.
   * Makes audit of that save.
   *
   * @param user the User entity to be persisted.
   * @param modifiedByUser user that made the change to User entity.
   * @return saved User.
   */
  @Transactional
  public User update(User user, User modifiedByUser) {
    var previousUserState = UserAudit.fromUser(getUserById(user.getId()));
    saveAuditInformation(OperationPerformed.UPDATE, modifiedByUser, user, previousUserState);

    return userRepository.save(user);
  }

  /**
   * Saves the provided User to repository.
   * Makes audit of that save.
   *
   * @param user the User entity to be persisted.
   * @param modifiedByUser user that made the change to User entity.
   * @return saved User.
   */
  @Transactional
  public User create(User user, User modifiedByUser) {
    if(userRepository.existsByEmail(user.getEmail())){
      logAndThrowEntityExistsException(user);
    }
    saveAuditInformation(OperationPerformed.CREATE, modifiedByUser, user, null);

    return userRepository.save(user);
  }

  /**
   * Sets user to be active. When user is created he is initially inactive.
   * Only inactive users can become active.
   * Once user is deleted, he can only turn back to being inactive, and then active.
   */
  public void activate(Long id, User modifiedByUser) {
    var user = userRepository.getOne(id);
    var previousUserState = UserAudit.fromUser(user);

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
    saveAuditInformation(OperationPerformed.ACTIVATE, modifiedByUser, user, previousUserState);
  }

  /**
   * Logically deletes User.
   */
  @Transactional
  public void delete(Long userId, User modifiedByUser) {
    var user = userRepository.getOne(userId);
    var previousUserState = UserAudit.fromUser(user);
    user.setUserStatusType(UserStatusType.DELETED);
    userRepository.save(user);
    saveAuditInformation(OperationPerformed.DELETE, modifiedByUser, user, previousUserState);
  }

  /**
   * Gets users leave profile and currently active years, and creates allowances
   * according with values from leave profile
   */
  public User createAllowanceForUserOnCreateUser(User user, User modifiedByUser) {
    var leaveProfile = leaveProfileRepository.getOne(user.getLeaveProfile().getId());
    var openedActiveYears = yearRepository.findAllByYearGreaterThanEqual(LocalDate.now().getYear());
    List<Allowance> userAllowances = new ArrayList<>();
    for (Year year : openedActiveYears) {
      Allowance allowance = createAllowance(user, year, leaveProfile);
      userAllowances.add(allowance);
    }
    user.setAllowances(userAllowances);
    return create(user, modifiedByUser);
  }

  /**
   * Updates values for allowances for active years. Since Leave Profile was just updated, values
   * for already created allowances for currently active years need to be updated as well.
   * If date of Leave Profile update was before half year mark, only half of allowance bonus should
   * be added, and full amount if it was afterwards. Following years allowances receive full amount.
   *
   * @param user the User entity for witch allowances should be updated.
   * @param previousLeaveProfile the LeaveProfile entity necessary for determining difference in allowance.
   * @param todaysDate year, month and date for today.
   */
  public void updateAllowanceForUserOnLeaveProfileUpdate(User user, LeaveProfile previousLeaveProfile,
      LocalDate todaysDate) {
    var openedActiveYears = yearRepository.findAllByYearGreaterThanEqual(todaysDate.getYear());
    var userAllowances = user.getAllowances();
    var nextYearBonus = user.getLeaveProfile().getEntitlement() - previousLeaveProfile.getEntitlement();
    int thisYearBonus = (MonthDay.from(todaysDate).isBefore(HALF_YEAR_DATE)) ? (nextYearBonus / 2) : nextYearBonus;

    for (Year year : openedActiveYears) {
      for (Allowance allowance : userAllowances) {

        if (todaysDate.getYear() == year.getYear() && allowance.getYear().equals(year)) {
          allowance.setAnnual(allowance.getAnnual() + thisYearBonus);

        } else if (todaysDate.getYear() != year.getYear() && allowance.getYear().equals(year)) {
          allowance.setAnnual(allowance.getAnnual() + nextYearBonus);
        }
      }
    }
    userRepository.save(user);
  }

  /**
   * If year when user started working at execom is not the same as current year that means
   * that next year is open, and allowances are created solely based on  users leave profile.
   * If year is same as current than this is the year when user started working at execom and
   * allowances are created based on remaining days in this year in reference to users leave profile.
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

  private void saveAuditInformation(OperationPerformed operationPerformed, User modifiedByUser, User modifiedUser,
      UserAudit previousUserState) {
    var currentUserState = UserAudit.fromUser(modifiedUser);

    if (OperationPerformed.CREATE.equals(operationPerformed)) {
      auditInformationService.saveAudit(operationPerformed, modifiedByUser, null, previousUserState, currentUserState);
    } else {
      auditInformationService.saveAudit(operationPerformed, modifiedByUser, modifiedUser, previousUserState,
          currentUserState);
    }
  }

  private void logAndThrowEntityExistsException(User user) {
    log.error("User with email {} already exists.", user.getEmail());
    throw new EntityExistsException("User with email " + user.getEmail() + " already exists.");
  }

}