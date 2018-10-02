package eu.execom.hawaii.service;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import eu.execom.hawaii.repository.DayRepository;
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
import eu.execom.hawaii.model.enumerations.AbsenceType;
import eu.execom.hawaii.model.enumerations.RequestStatus;
import eu.execom.hawaii.repository.AllowanceRepository;
import eu.execom.hawaii.repository.PublicHolidayRepository;

@RunWith(MockitoJUnitRunner.class)
public class AllowanceServiceTest {

  @Mock
  private AllowanceRepository allowanceRepository;

  @Mock
  private PublicHolidayRepository publicHolidayRepository;

  @Mock
  private DayRepository dayRepository;

  @InjectMocks
  private AllowanceService allowanceService;

  private User mockUser;
  private Allowance currentYearAllowance;

  @Before
  public void setUp() {
    mockUser = EntityBuilder.user(EntityBuilder.team());
    currentYearAllowance = EntityBuilder.allowance(mockUser);
  }

  @Test
  public void shouldGetAllowanceByUser() {
    // given
    given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2018)).willReturn(currentYearAllowance);

    // when
    Allowance allowance = allowanceService.getByUserAndYear(mockUser.getId(), 2018);

    // then
    assertThat("Expected year to be 2018", allowance.getYear(), is(2018));
    verify(allowanceRepository).findByUserIdAndYear(anyLong(), anyInt());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyAnnualLeaveRequestOnCurrentAndNextYear() {
    // given
    var dayOne = EntityBuilder.day(LocalDate.of(2018, 11, 25));
    var dayTwo = EntityBuilder.day(LocalDate.of(2018, 11, 28));
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), Arrays.asList(dayOne, dayTwo));

    var startYearFrom = LocalDate.of(2018, 1, 1);
    var endYearTo = LocalDate.of(2018, 12, 31);
    var nextYearAllowance = EntityBuilder.allowance(mockUser);
    nextYearAllowance.setId(2L);
    nextYearAllowance.setYear(2019);

    given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2018)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2019)).willReturn(nextYearAllowance);
    given(publicHolidayRepository.findAllByDateIsBetween(startYearFrom, endYearTo)).willReturn(
        List.of(EntityBuilder.publicholiday()));

    // when
    allowanceService.applyRequest(request, false);

    // then
    verify(allowanceRepository, times(2)).findByUserIdAndYear(anyLong(), anyInt());
    verify(allowanceRepository).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  // Also same for pending

  @Test
  public void shouldApplyAnnualLeaveRequestOnNextYearAllowance() {
    // given
    var dayOne = EntityBuilder.day(LocalDate.of(2018, 11, 26));
    var dayTwo = EntityBuilder.day(LocalDate.of(2018, 11, 27));
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), List.of(dayOne, dayTwo));

    currentYearAllowance.setTakenAnnual(200);
    var nextYearAllowance = EntityBuilder.allowance(mockUser);
    nextYearAllowance.setId(2L);
    nextYearAllowance.setYear(2019);

    given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2018)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2019)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyRequest(request, false);

    // then
    verify(allowanceRepository, times(2)).findByUserIdAndYear(anyLong(), anyInt());
    verify(allowanceRepository, times(2)).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyAnnualLeaveRequestCancellationNextYear() {
    // given
    var dayOne = EntityBuilder.day(LocalDate.of(2018, 11, 26));
    var dayTwo = EntityBuilder.day(LocalDate.of(2018, 11, 27));
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), List.of(dayOne, dayTwo));

    currentYearAllowance.setTakenAnnual(200);
    var nextYearAllowance = EntityBuilder.allowance(mockUser);
    nextYearAllowance.setId(2L);
    nextYearAllowance.setYear(2019);

    given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2018)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2019)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyRequest(request, true);

    // then
    verify(allowanceRepository, times(2)).findByUserIdAndYear(anyLong(), anyInt());
    verify(allowanceRepository, times(2)).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyAnnualLeaveRequestCancellationCurrentAndNextYear() {
    // given
    var dayOne = EntityBuilder.day(LocalDate.of(2018, 11, 26));
    var dayTwo = EntityBuilder.day(LocalDate.of(2018, 11, 27));
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), List.of(dayOne, dayTwo));

    currentYearAllowance.setTakenAnnual(200);
    var nextYearAllowance = EntityBuilder.allowance(mockUser);
    nextYearAllowance.setId(2L);
    nextYearAllowance.setYear(2019);
    nextYearAllowance.setTakenAnnual(24);

    given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2018)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2019)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyRequest(request, true);

    // then
    verify(allowanceRepository, times(2)).findByUserIdAndYear(anyLong(), anyInt());
    verify(allowanceRepository).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFailToApplyRequest() {
    // given
    var absence = EntityBuilder.absenceAnnual();
    absence.setAbsenceSubtype(null);

    var dayOne = EntityBuilder.day(LocalDate.of(2018, 11, 25));
    var request = EntityBuilder.request(absence, List.of(dayOne));

    given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2018)).willReturn(currentYearAllowance);

    // when
    allowanceService.applyRequest(request, false);

  }

  @Test(expected = InsufficientHoursException.class)
  public void shouldFailToApplyAnnualRequestDueInsufficientHours() {
      //given
      var absence = EntityBuilder.absenceAnnual();
      absence.setAbsenceSubtype(AbsenceSubtype.ANNUAL);

      var allowance = EntityBuilder.allowance(mockUser);
      allowance.setTakenAnnual(160);
      allowance.setCarriedOver(0);

      var nextYearAllownace = EntityBuilder.allowance(mockUser);
      nextYearAllownace.setTakenAnnual(160);
      nextYearAllownace.setCarriedOver(0);

      var dayOne = EntityBuilder.day(LocalDate.of(2018, 10, 17));
      var dayTwo = EntityBuilder.day(LocalDate.of(2018, 10, 18));

      var request = EntityBuilder.request(absence, List.of(dayOne, dayTwo));

      given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2018)).willReturn(allowance);
      given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2019)).willReturn(nextYearAllownace);


      // when
      allowanceService.applyRequest(request, false);

  }

  @Test(expected = InsufficientHoursException.class)
  public void shouldFailToApplyTrainingRequestDueInsufficientHours() {
    //given
    var absence = EntityBuilder.absenceAnnual();
    absence.setAbsenceSubtype(AbsenceSubtype.TRAINING);

    var dayOne = EntityBuilder.day(LocalDate.of(2018, 11, 20));
    var dayTwo = EntityBuilder.day(LocalDate.of(2018, 11, 21));
    var dayThree = EntityBuilder.day(LocalDate.of(2018, 11, 22));
    var request = EntityBuilder.request(absence, List.of(dayOne, dayTwo, dayThree));

    given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2018)).willReturn(currentYearAllowance);

    //when
    allowanceService.applyRequest(request, false);

  }

  @Test
  public void shouldApplyPendingAnnualLeaveRequestOnCurrentAndNextYear() {
    // given
    var dayOne = EntityBuilder.day(LocalDate.of(2018, 11, 26));
    var dayTwo = EntityBuilder.day(LocalDate.of(2018, 11, 27));
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), List.of(dayOne, dayTwo));
    request.setRequestStatus(RequestStatus.PENDING);

    currentYearAllowance.setTakenAnnual(200);
    var nextYearAllowance = EntityBuilder.allowance(mockUser);
    nextYearAllowance.setId(2L);
    nextYearAllowance.setYear(2019);
    nextYearAllowance.setTakenAnnual(24);

    given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2018)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2019)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyPendingRequest(request, false);

    // then
    verify(allowanceRepository, times(2)).findByUserIdAndYear(anyLong(), anyInt());
    verify(allowanceRepository, times(2)).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyPendingAnnualLeaveRequestOnCurrentYear() {
    // given
    var dayOne = EntityBuilder.day(LocalDate.of(2018, 11, 26));
    var dayTwo = EntityBuilder.day(LocalDate.of(2018, 11, 27));
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), List.of(dayOne, dayTwo));
    request.setRequestStatus(RequestStatus.PENDING);

    currentYearAllowance.setTakenAnnual(160);
    var nextYearAllowance = EntityBuilder.allowance(mockUser);
    nextYearAllowance.setId(2L);
    nextYearAllowance.setYear(2019);

    given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2018)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2019)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyPendingRequest(request, false);

    // then
    verify(allowanceRepository, times(2)).findByUserIdAndYear(anyLong(), anyInt());
    verify(allowanceRepository).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyPendingAnnualLeaveCancellationCurrentAndNextYear() {
    // given
    var dayOne = EntityBuilder.day(LocalDate.of(2018, 11, 26));
    var dayTwo = EntityBuilder.day(LocalDate.of(2018, 11, 27));
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), List.of(dayOne, dayTwo));
    request.setRequestStatus(RequestStatus.PENDING);

    currentYearAllowance.setTakenAnnual(200);
    var nextYearAllowance = EntityBuilder.allowance(mockUser);
    nextYearAllowance.setId(2L);
    nextYearAllowance.setYear(2019);
    nextYearAllowance.setPendingAnnual(8);

    given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2018)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2019)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyPendingRequest(request, true);

    // then
    verify(allowanceRepository, times(2)).findByUserIdAndYear(anyLong(), anyInt());
    verify(allowanceRepository, times(2)).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyPendingAnnualLeaveCancellationCurrentYear() {
    // given
    var dayOne = EntityBuilder.day(LocalDate.of(2018, 11, 26));
    var dayTwo = EntityBuilder.day(LocalDate.of(2018, 11, 27));
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), List.of(dayOne, dayTwo));
    request.setRequestStatus(RequestStatus.PENDING);

    currentYearAllowance.setTakenAnnual(200);
    var nextYearAllowance = EntityBuilder.allowance(mockUser);
    nextYearAllowance.setId(2L);
    nextYearAllowance.setYear(2019);
    nextYearAllowance.setPendingAnnual(16);

    given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2018)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2019)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyPendingRequest(request, true);

    // then
    verify(allowanceRepository, times(2)).findByUserIdAndYear(anyLong(), anyInt());
    verify(allowanceRepository).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyPendingTraining() {
    // given
    var dayOne = EntityBuilder.day(LocalDate.of(2018, 11, 26));
    var dayTwo = EntityBuilder.day(LocalDate.of(2018, 11, 27));
    var request = EntityBuilder.request(EntityBuilder.absenceTraining(), List.of(dayOne, dayTwo));
    request.setRequestStatus(RequestStatus.PENDING);

    var nextYearAllowance = EntityBuilder.allowance(mockUser);
    given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2018)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2019)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyPendingRequest(request, false);

    // then
    verify(allowanceRepository, times(2)).findByUserIdAndYear(anyLong(), anyInt());
    verify(allowanceRepository).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyTraining() {
    // given
    var dayOne = EntityBuilder.day(LocalDate.of(2018, 11, 26));
    var dayTwo = EntityBuilder.day(LocalDate.of(2018, 11, 27));
    var request = EntityBuilder.request(EntityBuilder.absenceTraining(), List.of(dayOne, dayTwo));
    request.setRequestStatus(RequestStatus.APPROVED);

    var nextYearAllowance = EntityBuilder.allowance(mockUser);
    given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2018)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2019)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyRequest(request, false);

    // then
    verify(allowanceRepository, times(2)).findByUserIdAndYear(anyLong(), anyInt());
    verify(allowanceRepository).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplySickness() {
    var dayOne = EntityBuilder.day(LocalDate.of(2018, 11, 26));
    var dayTwo = EntityBuilder.day(LocalDate.of(2018, 11, 27));
    var absenceSickness = EntityBuilder.absence();
    absenceSickness.setAbsenceType(AbsenceType.SICKNESS);
    var request = EntityBuilder.request(absenceSickness, List.of(dayOne, dayTwo));
    request.setRequestStatus(RequestStatus.APPROVED);

    var nextYearAllowance = EntityBuilder.allowance(mockUser);
    given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2018)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2019)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyRequest(request, false);

    // then
    verify(allowanceRepository, times(2)).findByUserIdAndYear(anyLong(), anyInt());
    verify(allowanceRepository).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyBonusDays() {
    var dayOne = EntityBuilder.day(LocalDate.of(2018, 11, 26));
    var dayTwo = EntityBuilder.day(LocalDate.of(2018, 11, 27));
    var absenceBonusDays = EntityBuilder.absence();
    var request = EntityBuilder.request(absenceBonusDays, List.of(dayOne, dayTwo));
    request.setRequestStatus(RequestStatus.APPROVED);

    var nextYearAllowance = EntityBuilder.allowance(mockUser);
    given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2018)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYear(mockUser.getId(), 2019)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyRequest(request, false);

    // then
    verify(allowanceRepository, times(2)).findByUserIdAndYear(anyLong(), anyInt());
    verify(allowanceRepository).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

}