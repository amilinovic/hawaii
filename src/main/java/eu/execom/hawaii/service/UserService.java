package eu.execom.hawaii.service;

import eu.execom.hawaii.model.User;
import eu.execom.hawaii.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

/**
 *
 */
@Service
public class UserService {

  private UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Retrieves a list of all users from repository
   *
   * @return a list of all users
   */
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  /**
   * Retrieves a User with a specific id
   *
   * @param id User email
   * @return User with specified email if exists
   * @throws EntityNotFoundException if a user with given id is not found
   */
  @Transactional
  public User getUserById(Long id) {
    return userRepository.getOne(id);
  }

  /**
   * Retrieves a User with a specific email
   *
   * @param email User email
   * @return User with specified email if exists
   * @throws EntityNotFoundException if a user with given email is not found
   */
  @Transactional
  public User getUserByEmail(String email) {
    checkIfUserNotFound(email);
    return userRepository.findByEmail(email);
  }

  /**
   * Saves the provided User to repository.
   *
   * @param user the User entity to be persisted.
   * @throws EntityExistsException if a user with same email already exists
   */
  public User saveUser(User user) {
    checkIfUserAlreadyExists(user);
    return userRepository.save(user);
  }

  /**
   * Retrieves a User with a specific email
   *
   * @param user the User object to update
   * @return the updated user
   * @throws EntityNotFoundException if a user with given id is not found
   */
  public User updateUser(User user) {
    checkIfUserNotFound(user.getId());
    return userRepository.save(user);
  }

  /**
   * Sets user to inactive
   *
   * @param id - the user id
   * @throws EntityNotFoundException if a user with given id is not found
   */
  public void deleteUser(Long id) {
    User user = userRepository.getOne(id);
    user.setActive(false);
    userRepository.save(user);
  }

  private void checkIfUserAlreadyExists(User user) {
    User existingUser = userRepository.findByEmail(user.getEmail());
    if (existingUser != null) {
      throw new EntityExistsException();
    }
  }

  private void checkIfUserNotFound(Long id) {
    if (!userRepository.existsById(id)) {
      throw new EntityNotFoundException();
    }
  }

  private void checkIfUserNotFound(String email) {
    if (!userRepository.existsByEmail(email)) {
      throw new EntityNotFoundException();
    }
  }

}
