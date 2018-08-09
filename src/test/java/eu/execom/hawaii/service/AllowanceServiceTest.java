package eu.execom.hawaii.service;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import eu.execom.hawaii.model.Allowance;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.repository.AllowanceRepository;

@RunWith(MockitoJUnitRunner.class)
public class AllowanceServiceTest {

  @Mock
  private AllowanceRepository allowanceRepository;

  @InjectMocks
  private AllowanceService allowanceService;

  private User mockUser;
  private Allowance mockAllowance;

  @Before
  public void setUp() {
    mockUser = EntityBuilder.user(EntityBuilder.team());
    mockAllowance = EntityBuilder.allowance(mockUser);
  }

  @Test
  public void shouldGetAllowanceByUser() {
    // given
    given(allowanceRepository.findByUser(mockUser)).willReturn(mockAllowance);

    // when
    Allowance allowance = allowanceService.getByUser(mockUser);

    // then
    assertThat("", allowance.getYear(), is(2018));
    verify(allowanceRepository).findByUser(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyRequest() {
    // given
    var request = EntityBuilder.request(EntityBuilder.absence());
    var dayOne = EntityBuilder.day(LocalDate.of(2018, 11, 25));
    var dayTwo = EntityBuilder.day(LocalDate.of(2018, 11, 28));
    request.setDays(Arrays.asList(dayOne, dayTwo));

    given(allowanceRepository.findByUser(mockUser)).willReturn(mockAllowance);

    // when
    allowanceService.applyRequest(request, false);

    // then
    verify(allowanceRepository).findByUser(any());
    verify(allowanceRepository).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFailToApplyRequest() {
    // given
    var absence = EntityBuilder.absence();
    absence.setAbsenceSubtype(null);

    var request = EntityBuilder.request(EntityBuilder.absence());
    var dayOne = EntityBuilder.day(LocalDate.of(2018, 11, 25));
    request.setDays(Arrays.asList(dayOne));
    request.setAbsence(absence);

    given(allowanceRepository.findByUser(mockUser)).willReturn(mockAllowance);

    // when
    allowanceService.applyRequest(request, false);

  }

}