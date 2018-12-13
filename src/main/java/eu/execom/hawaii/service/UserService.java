package eu.execom.hawaii.service;

import eu.execom.hawaii.dto.CreateTokenDto;
import eu.execom.hawaii.model.Allowance;
import eu.execom.hawaii.model.LeaveProfile;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.UserPushToken;
import eu.execom.hawaii.model.Year;
import eu.execom.hawaii.model.enumerations.UserStatusType;
import eu.execom.hawaii.repository.AllowanceRepository;
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
import java.util.Collections;
import java.util.List;

/**
 * User management service.
 */
@Slf4j
@Service
public class UserService {

  private UserRepository userRepository;
  private LeaveProfileRepository leaveProfileRepository;
  private UserPushTokensRepository userPushTokensRepository;
  private AllowanceRepository allowanceRepository;
  private YearRepository yearRepository;

  @Autowired
  public UserService(UserRepository userRepository, LeaveProfileRepository leaveProfileRepository,
      UserPushTokensRepository userPushTokensRepository, AllowanceRepository allowanceRepository,
      YearRepository yearRepository) {
    this.userRepository = userRepository;
    this.leaveProfileRepository = leaveProfileRepository;
    this.userPushTokensRepository = userPushTokensRepository;
    this.allowanceRepository = allowanceRepository;
    this.yearRepository = yearRepository;
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

  private Allowance createAllowance(User user, Year year, LeaveProfile leaveProfile) {
    Allowance allowance = new Allowance();
    allowance.setUser(user);
    allowance.setYear(year);
    allowance.setAnnual(leaveProfile.getEntitlement());
    allowance.setTraining(leaveProfile.getTraining());
    allowanceRepository.save(allowance);

    return allowance;
  }

  /**
   * Each active user receives increment of one year of service on every year, on 1st of January
   */
  @Scheduled(cron = "0 1 1 * * *")
  public void addServiceYearsToUser() {
    List<User> users = userRepository.findAllByUserStatusTypeIn(Collections.singletonList(UserStatusType.ACTIVE));
    users.stream().forEach(user -> {
      user.setYearsOfService(user.getYearsOfService() + 1);
      userRepository.save(user);
    });
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