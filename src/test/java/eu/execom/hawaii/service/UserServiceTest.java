package eu.execom.hawaii.service;

import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.UserStatusType;
import eu.execom.hawaii.repository.LeaveProfileRepository;
import eu.execom.hawaii.repository.UserRepository;
import eu.execom.hawaii.repository.YearRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private LeaveProfileRepository leaveProfileRepository;

  @Mock
  private YearRepository yearRepository;

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
    allMocks = new Object[] {userRepository, leaveProfileRepository, yearRepository};
  }

  @Test
  public void shouldGetAllUsers() {
    // given
    var userStatusType = UserStatusType.ACTIVE;
    given(userRepository.findAllByUserStatusTypeIn(Collections.singletonList(userStatusType))).willReturn(initialUsers);

    // when
    List<User> users = userService.findAllByUserStatusType(Collections.singletonList(userStatusType));

    // then
    assertThat("Expect size to be two", users.size(), is(2));
    assertThat("Expect name to be Aria Stark", users.get(0).getFullName(), is("Aria Stark"));
    assertThat("Expect name to be John Snow", users.get(1).getFullName(), is("John Snow"));
    verify(userRepository).findAllByUserStatusTypeIn(any());
    verifyNoMoreInteractions(userRepository);
  }

  @Test
  public void shouldgetAllUsersByActiveAndEmailOrFullName() {
    //given
    var active = UserStatusType.ACTIVE;
    var searchQuery = "aria.stark@gmail.com";
    Pageable pageable = PageRequest.of(1, 1, Sort.Direction.ASC, "fullName");
    Page<User> pagedUsers = new PageImpl<>(List.of(mockUser));
    given(userRepository.findAllByUserStatusTypeAndEmailContainingOrFullNameContaining(active, searchQuery, searchQuery,
        pageable)).willReturn(pagedUsers);

    //when
    Page<User> users = userService.findAllByActiveAndEmailOrFullName(active, searchQuery, pageable);

    //then
    assertEquals(users.getContent().size(), 1);
    verify(userRepository).findAllByUserStatusTypeAndEmailContainingOrFullNameContaining(any(), anyString(),
        anyString(), any());
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
    var thisYear = EntityBuilder.thisYear();
    var nextYear = EntityBuilder.nextYear();
    var activeYears = List.of(thisYear, nextYear);
    given(leaveProfileRepository.getOne(1L)).willReturn(leaveProfile);
    given(yearRepository.findAllByYearGreaterThanEqual(thisYear.getYear())).willReturn(activeYears);
    given(userRepository.save(user)).willReturn(user);

    // when
    User userWithAllowance = userService.createAllowanceForUserOnCreateUser(user);

    // then
    assertThat("Expect to have two allowance created", userWithAllowance.getAllowances().size(), is(2));
    assertThat("Expect that annual allowance for the first year is less than 160",
        userWithAllowance.getAllowances().get(0).getTakenAnnual(), lessThan(160));

    assertThat("Expect second allowance to be for next year",
        userWithAllowance.getAllowances().get(1).getYear().getYear(), is(EntityBuilder.nextYear().getYear()));
    assertThat("Expect that annual allowance for the second year is 160",
        userWithAllowance.getAllowances().get(1).getAnnual(), is(160));

    verify(leaveProfileRepository).getOne(anyLong());
    verify(userRepository).save(any());
    verify(yearRepository).findAllByYearGreaterThanEqual(anyInt());
    verifyNoMoreInteractions(allMocks);
  }

  @Test
  public void shouldUpdateAllowanceForUserOnLeaveProfileUpdate() {
    // given
    var user1 = EntityBuilder.user(EntityBuilder.team());
    var user2 = EntityBuilder.approver();
    var user1Allowance = EntityBuilder.allowance(user1);
    var user2Allowance = EntityBuilder.allowanceII(user2);

    user1.setAllowances(List.of(user1Allowance));
    user2.setAllowances(List.of(user2Allowance));

    var activeYears = List.of(EntityBuilder.thisYear(), EntityBuilder.nextYear());
    given(yearRepository.findAllByYearGreaterThanEqual(anyInt())).willReturn(activeYears);

    // when
    userService.updateAllowanceForUserOnLeaveProfileUpdate(user1);
    userService.updateAllowanceForUserOnLeaveProfileUpdate(user2);

    //than
    assertThat("Expect annual allowance to be 168", user1.getAllowances().get(0).getAnnual(), is(168));
    assertThat("Expect annual allowance to be 176", user2.getAllowances().get(0).getAnnual(), is(176));
    verify(yearRepository, times(2)).findAllByYearGreaterThanEqual(anyInt());
    verify(userRepository, times(2)).save(any());
    verifyNoMoreInteractions(allMocks);
  }
}