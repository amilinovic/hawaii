package eu.execom.hawaii.service;

import eu.execom.hawaii.model.User;
import eu.execom.hawaii.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;

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
   * Retrieves a User with a specific Id
   *
   * @param id User id
   * @return User with specified id
   * @throws EntityNotFoundException if user does not exist
   */
  public User getUserById(Long id) {
    checkIfUserNotFound(id);
    return userRepository.getOne(id);
  }

  /**
   * Retrieves a User with a specific email
   *
   * @param email User email
   * @return User with specified email if exists
   * @throws EntityNotFoundException if user does not exist
   */
  public User getUserDtoByEmail(String email) {
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

  public User updateUser(User user) {
    checkIfUserNotFound(user.getEmail());
    return userRepository.save(user);
  }

  public void deleteUser(User user) {
    checkIfUserNotFound(user.getEmail());
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
