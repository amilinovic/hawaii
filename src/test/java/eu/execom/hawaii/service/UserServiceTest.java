package eu.execom.hawaii.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
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
import eu.execom.hawaii.repository.LeaveProfileRepository;
import eu.execom.hawaii.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private LeaveProfileRepository leaveProfileRepository;

  @InjectMocks
  private UserService userService;

  private User mockUser;
  private List<User> initialUsers;
  private Object[] allMocks;

  @Before
  public void setUp() {
    var mockTeam = EntityBuilder.team();
    mockUser = EntityBuilder.user(mockTeam);

    var mockTeam2 = EntityBuilder.team();
    mockTeam2.setName("My team2");
    var mockUser2 = EntityBuilder.user(mockTeam2);
    mockUser2.setFullName("John Snow");

    initialUsers = new ArrayList<>(Arrays.asList(mockUser, mockUser2));
    allMocks = new Object[] {userRepository, leaveProfileRepository};
  }

  @Test
  public void shouldGetAllUsers() {
    // given
    var active = true;
    given(userRepository.findAllByActive(active)).willReturn(initialUsers);

    // when
    List<User> users = userService.findAllByActive(active);

    // then
    assertThat("Expect size to be two", users.size(), is(2));
    assertThat("Expect name to be Aria Stark", users.get(0).getFullName(), is("Aria Stark"));
    assertThat("Expect name to be John Snow", users.get(1).getFullName(), is("John Snow"));
    verify(userRepository).findAllByActive(anyBoolean());
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
    User user = userService.findByEmail(email);

    // then
    assertThat("Expect email to be aria.stark@gmail.com", user.getEmail(), is(email));
    verify(userRepository).findByEmail(anyString());
    verifyNoMoreInteractions(allMocks);
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
    verifyNoMoreInteractions(allMocks);
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
    verifyNoMoreInteractions(allMocks);
  }

  @Test
  public void shouldCreateAllowanceForNewUser() {
    // given
    var user = EntityBuilder.user(EntityBuilder.team());
    var leaveProfile = EntityBuilder.leaveProfile();
    given(leaveProfileRepository.getOne(1L)).willReturn(leaveProfile);
    given(userRepository.save(user)).willReturn(user);

    // when
    User userWithAllowance = userService.createAllowanceForUser(user, 2018);

    // then
    assertThat("Expect to have two allowance created", userWithAllowance.getAllowances().size(), is(2));
    assertThat("Expect to second allowance be for 2019 year", userWithAllowance.getAllowances().get(1).getYear(), is(2019));
    verify(leaveProfileRepository).getOne(anyLong());
    verify(userRepository).save(any());
    verifyNoMoreInteractions(allMocks);
  }

  @Test
  public void shouldCreateAllowanceUserForNextYear() {
    // given
    var user = EntityBuilder.user(EntityBuilder.team());
    var allowanceOne = EntityBuilder.allowance(user);
    var allowanceTwo = EntityBuilder.allowance(user);
    allowanceTwo.setYear(2019);
    user.setAllowances(new ArrayList<>(List.of(allowanceOne, allowanceTwo)));

    var leaveProfile = EntityBuilder.leaveProfile();
    given(leaveProfileRepository.getOne(1L)).willReturn(leaveProfile);
    given(userRepository.save(user)).willReturn(user);

    // when
    User userWithAllowance = userService.createAllowanceForUser(user, 2020);

    // then
    assertThat("Expect to have three allowance created", userWithAllowance.getAllowances().size(), is(3));
    assertThat("Expect to third allowance be for 2020 year", userWithAllowance.getAllowances().get(2).getYear(),
        is(2020));
    verify(leaveProfileRepository).getOne(anyLong());
    verify(userRepository).save(any());
    verifyNoMoreInteractions(allMocks);
  }

  @Test
  public void shouldSkipCreationNewAllowanceForUserWithExistingYearAllowance() {
    // given
    var user = EntityBuilder.user(EntityBuilder.team());
    var allowanceOne = EntityBuilder.allowance(user);
    var allowanceTwo = EntityBuilder.allowance(user);
    allowanceTwo.setYear(2019);
    user.setAllowances(List.of(allowanceOne, allowanceTwo));

    var leaveProfile = EntityBuilder.leaveProfile();
    given(leaveProfileRepository.getOne(1L)).willReturn(leaveProfile);

    // when
    User userWithAllowance = userService.createAllowanceForUser(user, 2019);

    // then
    assertThat("Expect to have three allowance created", userWithAllowance.getAllowances().size(), is(2));
    assertThat("Expect to third allowance be for 2020 year", userWithAllowance.getAllowances().get(1).getYear(),
        is(2019));
    verify(leaveProfileRepository).getOne(anyLong());
    verifyNoMoreInteractions(allMocks);
  }

}