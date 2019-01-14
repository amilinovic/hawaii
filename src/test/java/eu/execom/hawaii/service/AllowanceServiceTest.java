package eu.execom.hawaii.service;

import eu.execom.hawaii.exceptions.InsufficientHoursException;
import eu.execom.hawaii.model.Allowance;
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
  private User mockUser;
  private Allowance currentYearAllowance;

  @Before
  public void setUp() {

    thisYear = EntityBuilder.thisYear().getYear();
    nextYear = EntityBuilder.nextYear().getYear();

    mockUser = EntityBuilder.user(EntityBuilder.team());
    currentYearAllowance = EntityBuilder.allowance(mockUser);
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
  public void shouldApplyAnnualLeaveRequestOnCurrentAndNextYear() {
    // given
    var dayOne = EntityBuilder.day(LocalDate.of(thisYear, 11, 25));
    var dayTwo = EntityBuilder.day(LocalDate.of(thisYear, 11, 28));
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), Arrays.asList(dayOne, dayTwo));

    var startYearFrom = LocalDate.of(thisYear, 1, 1);
    var endYearTo = LocalDate.of(thisYear, 12, 31);
    var nextYearAllowance = EntityBuilder.allowance(mockUser);
    nextYearAllowance.setId(2L);
    nextYearAllowance.setYear(EntityBuilder.nextYear());

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);
    given(publicHolidayRepository.findAllByDateIsBetween(startYearFrom, endYearTo)).willReturn(
        List.of(EntityBuilder.publicholiday()));

    // when
    allowanceService.applyRequest(request, false);

    // then
    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verify(allowanceRepository).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  // Also same for pending

  @Test
  public void shouldApplyAnnualLeaveRequestOnNextYearAllowance() {
    // given
    var dayOne = EntityBuilder.day(LocalDate.of(thisYear, 11, 26));
    var dayTwo = EntityBuilder.day(LocalDate.of(thisYear, 11, 27));
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), List.of(dayOne, dayTwo));

    currentYearAllowance.setTakenAnnual(200);
    var nextYearAllowance = EntityBuilder.allowance(mockUser);
    nextYearAllowance.setId(2L);
    nextYearAllowance.setYear(EntityBuilder.nextYear());

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyRequest(request, false);

    // then
    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verify(allowanceRepository, times(2)).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyAnnualLeaveRequestCancellationNextYear() {
    // given
    var dayOne = EntityBuilder.day(LocalDate.of(thisYear, 11, 26));
    var dayTwo = EntityBuilder.day(LocalDate.of(thisYear, 11, 27));
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), List.of(dayOne, dayTwo));

    currentYearAllowance.setTakenAnnual(200);
    var nextYearAllowance = EntityBuilder.allowance(mockUser);
    nextYearAllowance.setId(2L);
    nextYearAllowance.setYear(EntityBuilder.nextYear());

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyRequest(request, true);

    // then
    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verify(allowanceRepository, times(2)).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyAnnualLeaveRequestCancellationCurrentAndNextYear() {
    // given
    var dayOne = EntityBuilder.day(LocalDate.of(thisYear, 11, 26));
    var dayTwo = EntityBuilder.day(LocalDate.of(thisYear, 11, 27));
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), List.of(dayOne, dayTwo));

    currentYearAllowance.setTakenAnnual(200);
    var nextYearAllowance = EntityBuilder.allowance(mockUser);
    nextYearAllowance.setId(2L);
    nextYearAllowance.setYear(EntityBuilder.nextYear());
    nextYearAllowance.setTakenAnnual(24);

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyRequest(request, true);

    // then
    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verify(allowanceRepository).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFailToApplyRequest() {
    // given
    var absence = EntityBuilder.absenceAnnual();
    absence.setAbsenceSubtype(null);

    var dayOne = EntityBuilder.day(LocalDate.of(thisYear, 11, 25));
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

    var nextYearAllownace = EntityBuilder.allowance(mockUser);
    nextYearAllownace.setTakenAnnual(160);
    nextYearAllownace.setCarriedOver(0);

    var dayOne = EntityBuilder.day(LocalDate.of(thisYear, 10, 17));
    var dayTwo = EntityBuilder.day(LocalDate.of(thisYear, 10, 18));

    var request = EntityBuilder.request(absence, List.of(dayOne, dayTwo));

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(allowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllownace);

    // when
    allowanceService.applyRequest(request, false);

  }

  @Test(expected = InsufficientHoursException.class)
  public void shouldFailToApplyTrainingRequestDueInsufficientHours() {
    //given
    var absence = EntityBuilder.absenceAnnual();
    absence.setAbsenceSubtype(AbsenceSubtype.TRAINING);

    var dayOne = EntityBuilder.day(LocalDate.of(thisYear, 11, 20));
    var dayTwo = EntityBuilder.day(LocalDate.of(thisYear, 11, 21));
    var dayThree = EntityBuilder.day(LocalDate.of(thisYear, 11, 22));
    var request = EntityBuilder.request(absence, List.of(dayOne, dayTwo, dayThree));

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);

    //when
    allowanceService.applyRequest(request, false);

  }

  @Test
  public void shouldApplyPendingAnnualLeaveRequestOnCurrentAndNextYear() {
    // given
    var dayOne = EntityBuilder.day(LocalDate.of(thisYear, 11, 26));
    var dayTwo = EntityBuilder.day(LocalDate.of(thisYear, 11, 27));
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), List.of(dayOne, dayTwo));
    request.setRequestStatus(RequestStatus.PENDING);

    currentYearAllowance.setTakenAnnual(200);
    var nextYearAllowance = EntityBuilder.allowance(mockUser);
    nextYearAllowance.setId(2L);
    nextYearAllowance.setYear(EntityBuilder.nextYear());
    nextYearAllowance.setTakenAnnual(24);

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyPendingRequest(request, false);

    // then
    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verify(allowanceRepository, times(2)).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyPendingAnnualLeaveRequestOnCurrentYear() {
    // given
    var dayOne = EntityBuilder.day(LocalDate.of(thisYear, 11, 26));
    var dayTwo = EntityBuilder.day(LocalDate.of(thisYear, 11, 27));
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), List.of(dayOne, dayTwo));
    request.setRequestStatus(RequestStatus.PENDING);

    currentYearAllowance.setTakenAnnual(160);
    var nextYearAllowance = EntityBuilder.allowance(mockUser);
    nextYearAllowance.setId(2L);
    nextYearAllowance.setYear(EntityBuilder.nextYear());

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyPendingRequest(request, false);

    // then
    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verify(allowanceRepository).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyPendingAnnualLeaveCancellationCurrentAndNextYear() {
    // given
    var dayOne = EntityBuilder.day(LocalDate.of(thisYear, 11, 26));
    var dayTwo = EntityBuilder.day(LocalDate.of(thisYear, 11, 27));
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), List.of(dayOne, dayTwo));
    request.setRequestStatus(RequestStatus.PENDING);

    currentYearAllowance.setTakenAnnual(200);
    var nextYearAllowance = EntityBuilder.allowance(mockUser);
    nextYearAllowance.setId(2L);
    nextYearAllowance.setYear(EntityBuilder.nextYear());
    nextYearAllowance.setPendingAnnual(8);

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyPendingRequest(request, true);

    // then
    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verify(allowanceRepository, times(2)).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyPendingAnnualLeaveCancellationCurrentYear() {
    // given
    var dayOne = EntityBuilder.day(LocalDate.of(thisYear, 11, 26));
    var dayTwo = EntityBuilder.day(LocalDate.of(thisYear, 11, 27));
    var request = EntityBuilder.request(EntityBuilder.absenceAnnual(), List.of(dayOne, dayTwo));
    request.setRequestStatus(RequestStatus.PENDING);

    currentYearAllowance.setTakenAnnual(200);
    var nextYearAllowance = EntityBuilder.allowance(mockUser);
    nextYearAllowance.setId(2L);
    nextYearAllowance.setYear(EntityBuilder.nextYear());
    nextYearAllowance.setPendingAnnual(16);

    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyPendingRequest(request, true);

    // then
    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verify(allowanceRepository).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyPendingTraining() {
    // given
    var dayOne = EntityBuilder.day(LocalDate.of(thisYear, 11, 26));
    var dayTwo = EntityBuilder.day(LocalDate.of(thisYear, 11, 27));
    var request = EntityBuilder.request(EntityBuilder.absenceTraining(), List.of(dayOne, dayTwo));
    request.setRequestStatus(RequestStatus.PENDING);

    var nextYearAllowance = EntityBuilder.allowance(mockUser);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyPendingRequest(request, false);

    // then
    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verify(allowanceRepository).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyTraining() {
    // given
    var dayOne = EntityBuilder.day(LocalDate.of(thisYear, 11, 26));
    var dayTwo = EntityBuilder.day(LocalDate.of(thisYear, 11, 27));
    var request = EntityBuilder.request(EntityBuilder.absenceTraining(), List.of(dayOne, dayTwo));
    request.setRequestStatus(RequestStatus.APPROVED);

    var nextYearAllowance = EntityBuilder.allowance(mockUser);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyRequest(request, false);

    // then
    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verify(allowanceRepository).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplySickness() {
    var dayOne = EntityBuilder.day(LocalDate.of(thisYear, 11, 26));
    var dayTwo = EntityBuilder.day(LocalDate.of(thisYear, 11, 27));
    var absenceSickness = EntityBuilder.absence();
    absenceSickness.setAbsenceType(AbsenceType.SICKNESS);
    var request = EntityBuilder.request(absenceSickness, List.of(dayOne, dayTwo));
    request.setRequestStatus(RequestStatus.APPROVED);

    var nextYearAllowance = EntityBuilder.allowance(mockUser);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyRequest(request, false);

    // then
    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verify(allowanceRepository).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldApplyBonusDays() {
    var dayOne = EntityBuilder.day(LocalDate.of(thisYear, 11, 26));
    var dayTwo = EntityBuilder.day(LocalDate.of(thisYear, 11, 27));
    var absenceBonusDays = EntityBuilder.absence();
    var request = EntityBuilder.request(absenceBonusDays, List.of(dayOne, dayTwo));
    request.setRequestStatus(RequestStatus.APPROVED);

    var nextYearAllowance = EntityBuilder.allowance(mockUser);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), thisYear)).willReturn(currentYearAllowance);
    given(allowanceRepository.findByUserIdAndYearYear(mockUser.getId(), nextYear)).willReturn(nextYearAllowance);

    // when
    allowanceService.applyRequest(request, false);

    // then
    verify(allowanceRepository, times(2)).findByUserIdAndYearYear(anyLong(), anyInt());
    verify(allowanceRepository).save(any());
    verifyNoMoreInteractions(allowanceRepository);
  }

  @Test
  public void shouldGetAllowancesForUser() {
    //given
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