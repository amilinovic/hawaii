package eu.execom.hawaii.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
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

  private User mockUser;
  private List<User> initialUsers;

  @Before
  public void setUp() {
    var mockTeam = EntityBuilder.team();
    mockUser = EntityBuilder.user(mockTeam);

    var mockTeam2 = EntityBuilder.team();
    mockTeam2.setName("My team2");
    var mockUser2 = EntityBuilder.user(mockTeam2);
    mockUser2.setFullName("John Snow");

    initialUsers = new ArrayList<>(Arrays.asList(mockUser, mockUser2));
  }

  @Test
  public void shouldGetAllUsers() {
    // given
    given(userRepository.findAll()).willReturn(initialUsers);

    // when
    List<User> users = userService.getAll();

    // then
    assertThat("Expect size to be two", users.size(), is(2));
    assertThat("Expect name to be Aria Stark", users.get(0).getFullName(), is("Aria Stark"));
    assertThat("Expect name to be John Snow", users.get(1).getFullName(), is("John Snow"));
    verify(userRepository).findAll();
    verifyNoMoreInteractions(userRepository);
  }

  @Test
  public void shouldGetUserById() {
    // given
    var userId = 1L;
    given(userRepository.getOne(userId)).willReturn(mockUser);

    // when
    User user = userService.getUserById(userId);

    // then
    assertThat("Expect name to be Aria Stark", user.getFullName(), is("Aria Stark"));
    verify(userRepository).getOne(anyLong());
    verifyNoMoreInteractions(userRepository);
  }

  @Test
  public void shouldGetUserByEmail() {
    // given
    var email = "aria.stark@gmail.com";
    given(userRepository.findByEmail(email)).willReturn(Optional.of(mockUser));

    // when
    User user = userService.getByEmail(email);

    // then
    assertThat("Expect email to be aria.stark@gmail.com", user.getEmail(), is(email));
    verify(userRepository).findByEmail(anyString());
    verifyNoMoreInteractions(userRepository);
  }

  @Test
  public void shouldSaveUser() {
    // given
    given(userRepository.save(mockUser)).willReturn(mockUser);

    // when
    User savedUser = userService.save(mockUser);

    // then
    assertThat("Expect name match to Aria Stark", savedUser.getFullName(), is(mockUser.getFullName()));
    verify(userRepository).save(any());
    verifyNoMoreInteractions(userRepository);
  }

  @Test
  public void shouldDeleteUser() {
    // given
    var userId = 1L;
    given(userRepository.getOne(userId)).willReturn(mockUser);

    // when
    userService.delete(userId);

    // then
    verify(userRepository).getOne(anyLong());
    verify(userRepository).save(any());
    verifyNoMoreInteractions(userRepository);
  }

}