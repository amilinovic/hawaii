package eu.execom.hawaii.service;

import eu.execom.hawaii.model.User;
import eu.execom.hawaii.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * User management service.
 */
@Service
public class UserService {

  private UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Retrieves a list of all users from repository.
   *
   * @return a list of all users
   */
  public List<User> getAll() {
    return userRepository.findAll();
  }

  /**
   * Retrieves a User with a specific id
   *
   * @param id User email
   * @return User with specified email if exists
   * @throws EntityNotFoundException if a user with given id is not found
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
  public User getByEmail(String email) {
    checkIfNotFound(email);
    return userRepository.findByEmail(email);
  }

  /**
   * Saves the provided User to repository.
   *
   * @param user the User entity to be persisted.
   * @throws EntityExistsException if a user with same email already exists
   */
  public User save(User user) {
    checkIfAlreadyExists(user);
    return userRepository.save(user);
  }

  /**
   * Retrieves a User with a specific email.
   *
   * @param user the User object to update
   * @return the updated user
   * @throws EntityNotFoundException if a user with given id is not found
   */
  public User update(User user) {
    checkIfNotFound(user.getId());
    return userRepository.save(user);
  }

  /**
   * Logically deletes User.
   *
   * @param id - the user id
   * @throws EntityNotFoundException if a user with given id is not found
   */
  public void delete(Long id) {
    User user = userRepository.getOne(id);
    user.setActive(false);
    userRepository.save(user);
  }

  private void checkIfAlreadyExists(User user) {
    User existingUser = userRepository.findByEmail(user.getEmail());
    if (existingUser != null) {
      throw new EntityExistsException();
    }
  }

  private void checkIfNotFound(Long id) {
    if (!userRepository.existsById(id)) {
      throw new EntityNotFoundException();
    }
  }

  private void checkIfNotFound(String email) {
    if (!userRepository.existsByEmail(email)) {
      throw new EntityNotFoundException();
    }
  }

}
