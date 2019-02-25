package eu.execom.hawaii.jobs;

import eu.execom.hawaii.model.Allowance;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.service.AllowanceService;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class CarryOverVacationJobTest {

  @Mock
  private UserService userService;

  @Mock
  private AllowanceService allowanceService;

  @InjectMocks
  private CarryOverVacationJob carryOverVacationJob;

  private User mockUser;
  private Object[] allMocks;

  @Before
  public void setUp() {
    mockUser = EntityBuilder.user(EntityBuilder.team());
    allMocks = new Object[] {userService, allowanceService};
  }

  @Test
  public void shouldAddMaxAllowedCarriedOverToUsers() {
    //given
    Allowance allowance = EntityBuilder.allowance(mockUser);
    var activeUsers = List.of(mockUser);
    given(userService.findAllByUserStatusType(any())).willReturn(activeUsers);
    given(allowanceService.getByUserAndYear(any(), anyInt())).willReturn(allowance);
    given(allowanceService.calculateRemainingAnnualHours(allowance)).willReturn(100);

    //when
    carryOverVacationJob.addCarriedOverToUsers();

    //than
    assertThat("Expect carried over is 40 hours", allowance.getCarriedOver(), is(40));
    verify(userService).findAllByUserStatusType(any());
    verify(allowanceService, times(2)).getByUserAndYear(any(), anyInt());
    verify(allowanceService).calculateRemainingAnnualHours(any());
    verify(allowanceService).save(any());
    verifyNoMoreInteractions(allMocks);
  }

  @Test
  public void shouldAddCarriedOverToUsers() {
    //given
    Allowance allowance = EntityBuilder.allowance(mockUser);
    var activeUsers = List.of(mockUser);
    given(userService.findAllByUserStatusType(any())).willReturn(activeUsers);
    given(allowanceService.getByUserAndYear(any(), anyInt())).willReturn(allowance);
    given(allowanceService.calculateRemainingAnnualHours(allowance)).willReturn(36);

    //when
    carryOverVacationJob.addCarriedOverToUsers();

    //than
    assertThat("Expect carried over is 36 hours", allowance.getCarriedOver(), is(36));
    verify(userService).findAllByUserStatusType(any());
    verify(allowanceService, times(2)).getByUserAndYear(any(), anyInt());
    verify(allowanceService).calculateRemainingAnnualHours(any());
    verify(allowanceService).save(any());
    verifyNoMoreInteractions(allMocks);
  }

  @Test
  public void shouldNotAddCarriedOverToUsers() {
    //given
    Allowance allowance = EntityBuilder.allowance(mockUser);
    var activeUsers = List.of(mockUser);
    given(userService.findAllByUserStatusType(any())).willReturn(activeUsers);
    given(allowanceService.getByUserAndYear(any(), anyInt())).willReturn(allowance);
    given(allowanceService.calculateRemainingAnnualHours(allowance)).willReturn(0);

    //when
    carryOverVacationJob.addCarriedOverToUsers();

    //than
    assertThat("Expect carried over is 0 hours", allowance.getCarriedOver(), is(0));
    verify(userService).findAllByUserStatusType(any());
    verify(allowanceService, times(2)).getByUserAndYear(any(), anyInt());
    verify(allowanceService).calculateRemainingAnnualHours(any());
    verify(allowanceService).save(any());
    verifyNoMoreInteractions(allMocks);
  }

}