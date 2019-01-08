package eu.execom.hawaii.jobs;

import eu.execom.hawaii.model.Allowance;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.repository.AllowanceRepository;
import eu.execom.hawaii.repository.UserRepository;
import eu.execom.hawaii.service.AllowanceService;
import eu.execom.hawaii.service.EntityBuilder;
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
  private UserRepository userRepository;

  @Mock
  private AllowanceRepository allowanceRepository;

  @Mock
  private AllowanceService allowanceService;

  @InjectMocks
  private CarryOverVacationJob carryOverVacationJob;

  private User mockUser;
  private Object[] allMocks;

  @Before
  public void setUp() {
    mockUser = EntityBuilder.user(EntityBuilder.team());
    allMocks = new Object[] {userRepository, allowanceRepository, allowanceService};
  }

  @Test
  public void shouldAddCarriedOverToUsers() {
    //given
    Allowance allowance = EntityBuilder.allowance(mockUser);
    var activeUsers = List.of(mockUser);
    given(userRepository.findAllByUserStatusTypeIn(any())).willReturn(activeUsers);
    given(allowanceRepository.findByUserIdAndYear(any(), anyInt())).willReturn(allowance);
    given(allowanceService.calculateRemainingAnnualHours(allowance)).willReturn(100);

    //when
    carryOverVacationJob.addCarriedOverToUsers();

    //than
    assertThat("Expect carried over is 40 hours", allowance.getCarriedOver(), is(40));
    verify(userRepository).findAllByUserStatusTypeIn(any());
    verify(allowanceRepository, times(2)).findByUserIdAndYear(any(), anyInt());
    verify(allowanceService).calculateRemainingAnnualHours(any());
    verify(allowanceRepository).save(any());
    verifyNoMoreInteractions(allMocks);
  }
}