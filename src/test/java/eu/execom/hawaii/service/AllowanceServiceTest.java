package eu.execom.hawaii.service;

import eu.execom.hawaii.exceptions.InsufficientHoursException;
import eu.execom.hawaii.model.Allowance;
import eu.execom.hawaii.model.Day;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.AbsenceSubtype;
import eu.execom.hawaii.model.enumerations.AbsenceType;
import eu.execom.hawaii.model.enumerations.RequestStatus;
import eu.execom.hawaii.repository.AllowanceRepository;
import eu.execom.hawaii.repository.PublicHolidayRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class AllowanceServiceTest {

  @Mock
  private AllowanceRepository allowanceRepository;

  @Mock
  private PublicHolidayRepository publicHolidayRepository;

  @InjectMocks
  private AllowanceService allowanceService;

  private int thisYear;
  private int nextYear;
  private Day dayOne;
  private Day dayTwo;
  private LocalDateTime submissionTime;
  private User mockUser;
  private Allowance currentYearAllowance;
  private Allowance nextYearAllowance;

  @Before
  public void setUp() {
    thisYear = EntityBuilder.thisYear().getYear();
    nextYear = EntityBuilder.nextYear().getYear();
    mockUser = EntityBuilder.user(EntityBuilder.team());
    dayOne = EntityBuilder.day(LocalDate.of(thisYear, 11, 26));
    dayTwo = EntityBuilder.day(LocalDate.of(thisYear, 11, 27));
    submissionTime = LocalDateTime.of(dayOne.getDate(), LocalTime.NOON);
    currentYearAllowance = EntityBuilder.allowance(mockUser);
    nextYearAllowance = EntityBuilder.nextYearAllowance(mockUser);
  }

  @Test
  public void shouldGetAllowanceByUser() {
    // given
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);

    // when
    Allowance allowance = allowanceService.getByUserAndYear(mockUser.getId(), thisYear);

    // then
    assertThat("Expected year to be is this year", allowance.getYear().getYear(),
        is(EntityBuilder.thisYear().getYear()));
    verify(allowanceRepository).findByUserIdAndYearYear(anyLong(), anyInt());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyAnnualLeaveRequestOnCurrentYearAllowance() {
    // given
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), List.of(dayOne, dayTwo));
    currentYearAllowance.setTakenAnnual(184);
    nextYearAllowance.setTakenAnnual(0);

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyRequest(request, false);

    // then
    assertThat("Expect taken annual hours for this year to be 200", currentYearAllowance.getTakenAnnual(), is(200));
    assertThat("Expect taken annual hours for next year to be 0", nextYearAllowance.getTakenAnnual(), is(0));
    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verify(allowanceRepository).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyAnnualLeaveRequestOnNextYearAllowance() {
    // given
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), List.of(dayOne, dayTwo));
    currentYearAllowance.setTakenAnnual(200);
    nextYearAllowance.setTakenAnnual(0);

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyRequest(request, false);

    // then
    assertThat("Expect taken annual hours for this year to be 200", currentYearAllowance.getTakenAnnual(), is(200));
    assertThat("Expect taken annual hours for next year to be 16", nextYearAllowance.getTakenAnnual(), is(16));
    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verify(allowanceRepository, times(2)).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyAnnualLeaveRequestOnCurrentAndNextYear() {
    // given
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), Arrays.asList(dayOne, dayTwo));
    var startYearFrom = LocalDate.of(thisYear, 1, 1);
    var endYearTo = LocalDate.of(thisYear, 12, 31);
    currentYearAllowance.setTakenAnnual(192);
    nextYearAllowance.setTakenAnnual(8);

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);
    given(publicHolidayRepository.findAllByDateIsBetween(startYearFrom, endYearTo)).willReturn(
        List.of(EntityBuilder.publicholiday()));

    // when
    allowanceService.applyRequest(request, false);

    // then
    assertThat("Expect taken annual hours for this year to be 200", currentYearAllowance.getTakenAnnual(), is(200));
    assertThat("Expect taken annual hours for next year to be 16", nextYearAllowance.getTakenAnnual(), is(16));
    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verify(allowanceRepository, times(2)).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyAnnualLeaveRequestCancellationCurrentYear() {
    // given
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), List.of(dayOne, dayTwo));
    currentYearAllowance.setTakenAnnual(200);
    nextYearAllowance.setTakenAnnual(0);

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyRequest(request, true);

    // then
    assertThat("Expect taken annual hours for this year to be 184", currentYearAllowance.getTakenAnnual(), is(184));
    assertThat("Expect taken annual hours for next year to be 0", nextYearAllowance.getTakenAnnual(), is(0));
    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verify(allowanceRepository, times(2)).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyAnnualLeaveRequestCancellationNextYear() {
    // given
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), List.of(dayOne, dayTwo));
    currentYearAllowance.setTakenAnnual(200);
    nextYearAllowance.setTakenAnnual(24);

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyRequest(request, true);

    // then
    assertThat("Expect taken annual hours for this year to be 200", currentYearAllowance.getTakenAnnual(), is(200));
    assertThat("Expect taken annual hours for next year to be 8", nextYearAllowance.getTakenAnnual(), is(8));
    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verify(allowanceRepository).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyAnnualLeaveRequestCancellationCurrentAndNextYear() {
    // given
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), List.of(dayOne, dayTwo));
    currentYearAllowance.setTakenAnnual(200);
    nextYearAllowance.setTakenAnnual(8);

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyRequest(request, true);

    // then

    assertThat("Expect taken annual hours for this year to be 192", currentYearAllowance.getTakenAnnual(), is(192));
    assertThat("Expect taken annual hours for next year to be 0", nextYearAllowance.getTakenAnnual(), is(0));
    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verify(allowanceRepository, times(2)).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFailToApplyRequest() {
    // given
    var absence = EntityBuilder.absenceAnnual();
    absence.setAbsenceSubtype(null);
    var request = EntityBuilder.request(absence, List.of(dayOne));

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);

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
    var request = EntityBuilder.request(absence, List.of(dayOne, dayTwo));
    nextYearAllowance.setTakenAnnual(160);
    nextYearAllowance.setCarriedOver(0);

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(allowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyRequest(request, false);
  }

  @Test(expected = InsufficientHoursException.class)
  public void shouldFailToApplyTrainingRequestDueInsufficientHours() {
    //given
    var absence = EntityBuilder.absenceAnnual();
    absence.setAbsenceSubtype(AbsenceSubtype.TRAINING);
    var dayThree = EntityBuilder.day(LocalDate.of(thisYear, 11, 28));
    var request = EntityBuilder.request(absence, List.of(dayOne, dayTwo, dayThree));

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);

    //when
    allowanceService.applyRequest(request, false);
  }

  @Test(expected = InsufficientHoursException.class)
  public void shouldFailToApplyPendingAnnualLeaveRequestDueInsufficientHours() {
    //given
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), List.of(dayOne, dayTwo));
    request.setSubmissionTime(submissionTime);
    currentYearAllowance.setTakenAnnual(200);
    nextYearAllowance.setTakenAnnual(40);

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    //when
    allowanceService.applyPendingRequest(request, false);
  }

  @Test
  public void shouldApplyPendingAnnualLeaveRequestOnCurrentYear() {
    // given
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), List.of(dayOne, dayTwo));
    request.setRequestStatus(RequestStatus.PENDING);
    request.setSubmissionTime(submissionTime);
    currentYearAllowance.setTakenAnnual(180);
    currentYearAllowance.setPendingAnnual(0);
    nextYearAllowance.setPendingAnnual(0);

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyPendingRequest(request, false);

    // then
    assertThat("Expect pending annual hours for this year to be 16", currentYearAllowance.getPendingAnnual(), is(16));
    assertThat("Expect pending annual hours for next year to be 0", nextYearAllowance.getPendingAnnual(), is(0));
    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verify(allowanceRepository).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyPendingAnnualLeaveRequestOnNextYear() {
    // given
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), List.of(dayOne, dayTwo));
    request.setRequestStatus(RequestStatus.PENDING);
    request.setSubmissionTime(submissionTime);
    currentYearAllowance.setTakenAnnual(200);
    currentYearAllowance.setPendingAnnual(0);
    nextYearAllowance.setPendingAnnual(0);

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyPendingRequest(request, false);

    // then
    assertThat("Expect pending annual hours for this year to be 0", currentYearAllowance.getPendingAnnual(), is(0));
    assertThat("Expect pending annual hours for next year to be 16", nextYearAllowance.getPendingAnnual(), is(16));
    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verify(allowanceRepository, times(2)).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyPendingAnnualLeaveRequestOnCurrentAndNextYear() {
    // given
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), List.of(dayOne, dayTwo));
    request.setRequestStatus(RequestStatus.PENDING);
    request.setSubmissionTime(submissionTime);
    currentYearAllowance.setTakenAnnual(192);
    nextYearAllowance.setTakenAnnual(24);
    currentYearAllowance.setPendingAnnual(0);
    nextYearAllowance.setPendingAnnual(0);

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyPendingRequest(request, false);

    // then
    assertThat("Expect pending hours for this year to be 8", currentYearAllowance.getPendingAnnual(), is(8));
    assertThat("Expect pending hours for next year to be 8", nextYearAllowance.getPendingAnnual(), is(8));
    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verify(allowanceRepository, times(2)).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyPendingAnnualLeaveCancellationCurrentYear() {
    // given
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), List.of(dayOne, dayTwo));
    request.setRequestStatus(RequestStatus.PENDING);
    request.setSubmissionTime(submissionTime);
    currentYearAllowance.setTakenAnnual(200);
    currentYearAllowance.setPendingAnnual(16);
    nextYearAllowance.setPendingAnnual(0);

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyPendingRequest(request, true);

    // then
    assertThat("Expect pending hours for this year to be 0", currentYearAllowance.getPendingAnnual(), is(0));
    assertThat("Expect pending hours for next year to be 0", nextYearAllowance.getPendingAnnual(), is(0));
    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verify(allowanceRepository, times(2)).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyPendingAnnualLeaveCancellationNextYear() {
    // given
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), List.of(dayOne, dayTwo));
    request.setRequestStatus(RequestStatus.PENDING);
    request.setSubmissionTime(submissionTime);
    currentYearAllowance.setTakenAnnual(200);
    currentYearAllowance.setPendingAnnual(0);
    nextYearAllowance.setPendingAnnual(16);

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyPendingRequest(request, true);

    // then
    assertThat("Expect pending hours for this year to be 0", currentYearAllowance.getPendingAnnual(), is(0));
    assertThat("Expect pending hours for next year to be 0", nextYearAllowance.getPendingAnnual(), is(0));
    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verify(allowanceRepository).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyPendingAnnualLeaveCancellationCurrentAndNextYear() {
    // given
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), List.of(dayOne, dayTwo));
    request.setRequestStatus(RequestStatus.PENDING);
    request.setSubmissionTime(submissionTime);
    currentYearAllowance.setTakenAnnual(200);
    currentYearAllowance.setPendingAnnual(8);
    nextYearAllowance.setPendingAnnual(8);

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyPendingRequest(request, true);

    // then
    assertThat("Expect pending hours for this year to be 0", currentYearAllowance.getPendingAnnual(), is(0));
    assertThat("Expect pending hours for next year to be 0", nextYearAllowance.getPendingAnnual(), is(0));
    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verify(allowanceRepository, times(2)).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyPendingTraining() {
    // given
    var time = LocalTime.now();
    var request = EntityBuilder.request(EntityBuilder.absenceTraining(), List.of(dayOne, dayTwo));
    request.setRequestStatus(RequestStatus.PENDING);
    request.setSubmissionTime(LocalDateTime.of(dayOne.getDate(), time));

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyPendingRequest(request, false);

    // then
    assertThat("Expect pending training hours to be 16", currentYearAllowance.getPendingTraining(), is(16));
    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verify(allowanceRepository).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test(expected = InsufficientHoursException.class)
  public void shouldFailToApplyPendingTrainingRequestDueInsufficientHours() {
    //given
    var absence = EntityBuilder.absenceAnnual();
    absence.setAbsenceSubtype(AbsenceSubtype.TRAINING);
    var dayThree = EntityBuilder.day(LocalDate.of(thisYear, 11, 28));
    var request = EntityBuilder.request(absence, List.of(dayOne, dayTwo, dayThree));
    request.setSubmissionTime(submissionTime);

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);

    //when
    allowanceService.applyPendingRequest(request, false);
  }

  @Test
  public void shouldApplyTraining() {
    // given
    var request = EntityBuilder.request(EntityBuilder.absenceTraining(), List.of(dayOne, dayTwo));
    request.setRequestStatus(RequestStatus.APPROVED);

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyRequest(request, false);

    // then
    assertThat("Expect training hours to be 16", currentYearAllowance.getTakenTraining(), is(16));
    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verify(allowanceRepository).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplySickness() {
    // given
    var absenceSickness = EntityBuilder.absence();
    absenceSickness.setAbsenceType(AbsenceType.SICKNESS);
    var request = EntityBuilder.request(absenceSickness, List.of(dayOne, dayTwo));

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyRequest(request, false);

    // then
    assertThat("Expect sick hours to be 16", currentYearAllowance.getSickness(), is(16));
    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verify(allowanceRepository).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyBonusDays() {
    // given
    var absenceBonusDays = EntityBuilder.absence();
    var request = EntityBuilder.request(absenceBonusDays, List.of(dayOne, dayTwo));
    request.setRequestStatus(RequestStatus.APPROVED);

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyRequest(request, false);

    // then
    assertThat("Expect bonus hours to be 16", currentYearAllowance.getBonus(), is(16));
    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verify(allowanceRepository).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test(expected = InsufficientHoursException.class)
  public void shouldFailToApplyBonusDays() {
    // given
    var absenceBonusDays = EntityBuilder.absence();
    var request = EntityBuilder.request(absenceBonusDays, List.of(dayOne, dayTwo));
    request.setRequestStatus(RequestStatus.APPROVED);
    currentYearAllowance.setBonus(30);

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyRequest(request, false);
  }

  @Test
  public void shouldGetAllowancesForUser() {
    // given
    currentYearAllowance.setTakenAnnual(100);
    var nextYearAllowance = EntityBuilder.allowanceII(mockUser);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    var allowanceForUserDto = allowanceService.getAllowancesForUser(mockUser);

    //then
    assertThat("Expect remaining annual hours to be 100", allowanceForUserDto.getRemainingAnnualHours(), is(100));
    assertThat("Expect next year remaining annual hours to be 40",
        allowanceForUserDto.getNextYearRemainingAnnualHours(), is(40));
    assertThat("Expect remaining training hours to be 40", allowanceForUserDto.getRemainingTrainingHours(), is(16));

    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verifyNoMoreInteractions(allowanceRepository);
  }
}