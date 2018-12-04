package eu.execom.hawaii.service;

import eu.execom.hawaii.dto.CreateTokenDto;
import eu.execom.hawaii.model.Allowance;
import eu.execom.hawaii.model.LeaveProfile;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.UserPushToken;
import eu.execom.hawaii.model.enumerations.UserStatusType;
import eu.execom.hawaii.repository.AllowanceRepository;
import eu.execom.hawaii.repository.LeaveProfileRepository;
import eu.execom.hawaii.repository.UserPushTokensRepository;
import eu.execom.hawaii.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
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

  @Autowired
  public UserService(UserRepository userRepository, LeaveProfileRepository leaveProfileRepository,
      UserPushTokensRepository userPushTokensRepository, AllowanceRepository allowanceRepository) {
    this.userRepository = userRepository;
    this.leaveProfileRepository = leaveProfileRepository;
    this.userPushTokensRepository = userPushTokensRepository;
    this.allowanceRepository = allowanceRepository;
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
  public List<User> findAllByUserStatusType(UserStatusType userStatusType) {
    return userRepository.findAllByUserStatusTypeIn(userStatusType);
  }

  /**
   * Retrieves a list of all users searched by given query.
   *
   * @param userStatusType what is user status (ACTIVE, INACTIVE or DELETED)
   * @param searchQuery search by given query.
   * @param pageable    the Pageable information about size per page and number of page.
   * @return a list of queried users by given search.
   */
  public Page<User> findAllByActiveAndEmailOrFullName(UserStatusType userStatusType, String searchQuery,
      Pageable pageable) {
    return userRepository.findAllByUserStatusTypeAndEmailContainingOrFullNameContaining(userStatusType, searchQuery,
        searchQuery,
        pageable);
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
   */
  public void activate(Long id) {
    var user = userRepository.getOne(id);
    user.setUserStatusType(UserStatusType.ACTIVE);
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
   * Assign new allowance to User based on users leave profile.
   *
   * @param user new User.
   */
  public User createAllowanceForUser(User user, int year) {
    var leaveProfile = leaveProfileRepository.getOne(user.getLeaveProfile().getId());
    var userAllowances = user.getAllowances();

    var userHasAllowanceForGivenYear = userAllowances.stream().anyMatch(allowance -> year == allowance.getYear());
    if (userHasAllowanceForGivenYear) {
      log.warn("User: {}, already has allowance for given year: {}", user.getEmail(), year);
      return user;
    }

    if (userAllowances.isEmpty()) {
      var currentYearAllowance = createAllowance(user, year, leaveProfile);
      var nextYearAllowance = createAllowance(user, year + 1, leaveProfile);
      userAllowances.addAll(List.of(currentYearAllowance, nextYearAllowance));

    } else {
      var allowance = createAllowance(user, year, leaveProfile);
      userAllowances.add(allowance);
    }
    return save(user);
  }

  private Allowance createAllowance(User user, int year, LeaveProfile leaveProfile) {
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
  @Scheduled(cron = "0 0 0 1 1 *")
  public void addServiceYearsToUser() {
    List<User> users = userRepository.findAllByUserStatusTypeIn(UserStatusType.ACTIVE);
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