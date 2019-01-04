package eu.execom.hawaii.jobs;

import eu.execom.hawaii.repository.LeaveProfileRepository;
import eu.execom.hawaii.repository.UserRepository;
import eu.execom.hawaii.repository.YearRepository;
import eu.execom.hawaii.service.EmailService;
import eu.execom.hawaii.service.EntityBuilder;
import eu.execom.hawaii.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class IncrementServiceYearsJobTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private LeaveProfileRepository leaveProfileRepository;

  @Mock
  private YearRepository yearRepository;

  @Mock
  private EmailService emailService;

  @Mock
  private UserService userService;

  @InjectMocks
  private IncrementServiceYearsJob incrementServiceYearsJob;

  private Object[] allMocks;

  @Before
  public void setUp() {
    allMocks = new Object[] {userRepository, leaveProfileRepository, yearRepository, emailService};
  }

  /**
   * Users started working date needs to be set to same month date as is today
   * in order for test to work.
   */
  @Test
  public void addServiceYearsToUser() {
    // given
    var user1 = EntityBuilder.user(EntityBuilder.team());
    var user2 = EntityBuilder.approver();
    var activeUsers = List.of(user1, user2);
    given(userRepository.findAllByUserStatusTypeIn(any())).willReturn(activeUsers);

    // when
    incrementServiceYearsJob.addServiceYearsToUser();

    //than
    assertThat("Expect year of service to be incremented to 5", user1.getYearsOfService(), is(5));
    assertThat("Expect leave profile id to be 2", user1.getLeaveProfile().getId(), is(2L));

    assertThat("Expect year of service to be incremented to 5", user2.getYearsOfService(), is(10));
    assertThat("Expect leave profile id to be 3", user2.getLeaveProfile().getId(), is(3L));

    verify(userRepository).findAllByUserStatusTypeIn(any());
    verify(emailService, times(2)).createLeaveProfileUpdateEmailAndSendForApproval(any());
    verify(userRepository, times(2)).save(any());
    verify(userService,times(2)).updateAllowanceForUserOnLeaveProfileUpdate(any());
    verifyNoMoreInteractions(allMocks);
  }
}