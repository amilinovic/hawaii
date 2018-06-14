package eu.execom.hawaii.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.execom.hawaii.model.User;
import eu.execom.hawaii.repository.UserRepository;

@Service
public class UserService {

  private UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public User getUser(Long id) {
    return userRepository.getOne(id);
  }

  public User getUserByEmail(String email) {
    User user = userRepository.findByEmail(email);
    if(user == null) {
      throw new EntityNotFoundException();
    }
    return user;
  }
}
