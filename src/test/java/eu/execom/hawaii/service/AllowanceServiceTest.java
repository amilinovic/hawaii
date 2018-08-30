package eu.execom.hawaii.service;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import eu.execom.hawaii.exceptions.InsufficientHoursException;
import eu.execom.hawaii.model.Allowance;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.AbsenceSubtype;
import eu.execom.hawaii.repository.AllowanceRepository;
import eu.execom.hawaii.repository.PublicHolidayRepository;

@RunWith(MockitoJUnitRunner.class)
public class AllowanceServiceTest {

  @Mock
  private AllowanceRepository allowanceRepository;

  @Mock
  private PublicHolidayRepository publicHolidayRepository;

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
    var dayOne = EntityBuilder.day(LocalDate.of(2018, 11, 25));
    var dayTwo = EntityBuilder.day(LocalDate.of(2018, 11, 28));
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), Arrays.asList(dayOne, dayTwo));

    var startYearFrom = LocalDate.of(2018, 01, 01);
    var endYearTo = LocalDate.of(2018, 12, 31);

    given(allowanceRepository.findByUser(mockUser)).willReturn(mockAllowance);
    given(publicHolidayRepository.findAllByDateIsBetween(startYearFrom, endYearTo)).willReturn(
        List.of(EntityBuilder.publicholiday()));

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
    var absence = EntityBuilder.absenceAnnual();
    absence.setAbsenceSubtype(null);

    var dayOne = EntityBuilder.day(LocalDate.of(2018, 11, 25));
    var request = EntityBuilder.request(absence, Arrays.asList(dayOne));

    given(allowanceRepository.findByUser(mockUser)).willReturn(mockAllowance);

    // when
    allowanceService.applyRequest(request, false);

  }

  @Test(expected = InsufficientHoursException.class)
  public void shouldFailToApplyRequestDueInsufficientHours() {
    //given
    var absence = EntityBuilder.absenceAnnual();
    absence.setAbsenceSubtype(AbsenceSubtype.TRAINING);

    var dayOne = EntityBuilder.day(LocalDate.of(2018, 11, 20));
    var dayTwo = EntityBuilder.day(LocalDate.of(2018, 11, 21));
    var dayThree = EntityBuilder.day(LocalDate.of(2018, 11, 22));
    var request = EntityBuilder.request(absence, Arrays.asList(dayOne, dayTwo, dayThree));

    given(allowanceRepository.findByUser(mockUser)).willReturn(mockAllowance);

    //when
    allowanceService.applyRequest(request, false);

  }

}