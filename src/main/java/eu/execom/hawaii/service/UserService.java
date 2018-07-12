package eu.execom.hawaii.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.execom.hawaii.model.User;
import eu.execom.hawaii.repository.UserRepository;

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
   * @throws EntityNotFoundException if a user with given id is not found
   */
  @Transactional
  public void delete(Long id) {
    var user = userRepository.getOne(id);
    user.setActive(false);
    userRepository.save(user);
  }

}
