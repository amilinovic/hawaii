package eu.execom.hawaii.service;

import eu.execom.hawaii.dto.CreateTokenDto;
import eu.execom.hawaii.model.Allowance;
import eu.execom.hawaii.model.LeaveProfile;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.UserPushToken;
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
   * @param active is it active.
   * @return a list of all users.
   */
  public List<User> findAllByActive(boolean active) {
    return userRepository.findAllByActive(active);
  }

  /**
   * Retrieves a list of all users searched by given query.
   *
   * @param active      is user active.
   * @param searchQuery search by given query.
   * @param pageable    the Pageable information about size per page and number of page.
   * @return a list of queried users by given search.
   */
  public Page<User> findAllByActiveAndEmailOrFullName(boolean active, String searchQuery, Pageable pageable) {
    return userRepository.findAllByActiveAndEmailContainingOrFullNameContaining(active, searchQuery, searchQuery,
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
   * Logically deletes User.
   *
   * @param id - the user id
   */
  @Transactional
  public void delete(Long id) {
    var user = userRepository.getOne(id);
    user.setActive(false);
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
    List<User> users = userRepository.findAllByActive(true);
    users.stream().forEach(user -> {
      user.setYearsOfService(user.getYearsOfService() + 1);
      userRepository.save(user);
    });
  }

  /**
   * Creates new Object userPushToken which has push token, platform from which user signed in and user who is the owner of that push token
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

    /*List<String> authUserPushTokens = authUser.getUserPushTokens()
                                              .stream()
                                              .map(UserPushToken::getPushToken)
                                              .collect(Collectors.toList());
    for (String token : authUserPushTokens) {
      if (!token.equals(createTokenDto.getPushToken())) {
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
    }*/

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