package eu.execom.hawaii.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.execom.hawaii.model.Allowance;
import eu.execom.hawaii.model.LeaveProfile;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.repository.LeaveProfileRepository;
import eu.execom.hawaii.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * User management service.
 */
@Slf4j
@Service
public class UserService {

  private UserRepository userRepository;
  private LeaveProfileRepository leaveProfileRepository;

  @Autowired
  public UserService(UserRepository userRepository, LeaveProfileRepository leaveProfileRepository) {
    this.userRepository = userRepository;
    this.leaveProfileRepository = leaveProfileRepository;
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
  @Transactional
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

    if (userAllowances == null) {
      var currentYearAllowance = createAllowance(user, year, leaveProfile);
      var nextYearAllowance = createAllowance(user, year + 1, leaveProfile);
      user.setAllowances(List.of(currentYearAllowance, nextYearAllowance));

      return save(user);
    }

    var isUserAlreadyHasAllowanceForGivenYear = userAllowances.stream().anyMatch(allowance -> year == allowance.getYear());
    if (isUserAlreadyHasAllowanceForGivenYear) {
      log.error("User: {}, already has allowance for given year: {}", user.getEmail(), year);

      return user;
    }

    var nextYearAllowance = createAllowance(user, year, leaveProfile);
    userAllowances.add(nextYearAllowance);
    user.setAllowances(userAllowances);

    return save(user);
  }

  private Allowance createAllowance(User user, int year, LeaveProfile leaveProfile) {
    Allowance allowance = new Allowance();
    allowance.setUser(user);
    allowance.setYear(year);
    allowance.setAnnual(leaveProfile.getEntitlement());
    allowance.setTraining(leaveProfile.getTraining());

    return allowance;
  }

}
