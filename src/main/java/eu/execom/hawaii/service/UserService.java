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
   * @param id
   * @return
   * @throws EntityNotFoundException if user does not exist
   */
  public User getUserById(Long id) {
    if (!userRepository.existsById(id)) {
      throw new EntityNotFoundException();
    }
    return userRepository.getOne(id);
  }

  /**
   * Retrieves a User with a specific email
   *
   * @param email
   * @return User with specified email if exists
   * @throws EntityNotFoundException if user does not exist
   */
  public User getUserByEmail(String email) {
    if (!userRepository.existsByEmail(email)) {
      throw new EntityNotFoundException();
    }
    return userRepository.findByEmail(email);
  }

  /**
   * Saves the provided User to repository.
   *
   * @param user the User entity to be persisted.
   * @throws EntityExistsException if a user with same email already exists
   */
  public void saveUser(User user) {
    checkIfUserAlreadyExists(user);
    userRepository.save(user);
  }

  public void updateUser(User user) {
    checkIfUserNotFound(user.getEmail());
    userRepository.save(user);
  }

  private void checkIfUserAlreadyExists(User user) {
    User existingUser = userRepository.findByEmail(user.getEmail());
    if (existingUser != null) {
      throw new EntityExistsException();
    }
  }

  private void checkIfUserNotFound(Long id) {
    userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
  }

  private void checkIfUserNotFound(String email) {
    User user = userRepository.findByEmail(email);
    if (user == null) {
      throw new EntityNotFoundException();
    }
  }

}
