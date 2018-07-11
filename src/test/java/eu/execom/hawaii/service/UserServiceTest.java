package eu.execom.hawaii.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import eu.execom.hawaii.model.User;
import eu.execom.hawaii.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  @Test
  public void shouldGetAllUsers() {
    // given

    // when
    List<User> users = userService.getAll();

    // then


  }

  @Test
  public void shouldGetUserById() {
    // given
    Long userId = 1L;

    // when
    User user = userService.getUserById(userId);

    // then
  }

  @Test
  public void shouldGetUserByEmail() {
    // given
    String email = "user@email.com";

    // when
    User user = userService.getByEmail(email);

    // then
  }

  @Test
  public void shouldSaveUser() {
    // given
    User user = new User();

    // when
    User savedUser = userService.save(user);

    // then
  }

  @Test
  public void shouldUpdateUser() {
    // given
    User user = new User();

    // when
    User updatedUser = userService.update(user);

    // then
  }

  @Test
  public void shouldDeleteUser() {
    // given
    Long userId = 1L;

    // when
    userService.delete(userId);

    // then
  }
}