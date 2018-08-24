package eu.execom.hawaii.service;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import eu.execom.hawaii.model.Absence;
import eu.execom.hawaii.model.Day;
import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.AbsenceType;
import eu.execom.hawaii.model.enumerations.RequestStatus;
import eu.execom.hawaii.repository.AbsenceRepository;
import eu.execom.hawaii.repository.RequestRepository;
import eu.execom.hawaii.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class RequestServiceTest {

  @Mock
  private AllowanceService allowanceService;
  @Mock
  private GoogleCalendarService googleCalendarService;
  @Mock
  private EmailService emailService;
  @Mock
  private RequestRepository requestRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private AbsenceRepository absenceRepository;
  @InjectMocks
  private RequestService requestService;

  private User mockUser;
  private Day dayOne;
  private Absence absenceAnnual;
  private Absence absenceTraining;
  private Request requestOne;
  private Request requestTwo;
  private List<Request> mockRequests;
  private Object[] allMocks;

  @Before
  public void setUp() {
    mockUser = EntityBuilder.user(EntityBuilder.team());

    absenceAnnual = EntityBuilder.absenceAnnual();
    absenceTraining = EntityBuilder.absenceTraining();

    dayOne = EntityBuilder.day(LocalDate.of(2018, 11, 20));
    var dayTwo = EntityBuilder.day(LocalDate.of(2018, 11, 21));
    var dayThree = EntityBuilder.day(LocalDate.of(2018, 11, 22));

    requestOne = EntityBuilder.request(absenceAnnual, List.of(dayOne));
    requestTwo = EntityBuilder.request(absenceTraining, Arrays.asList(dayTwo, dayThree));
    mockRequests = Arrays.asList(requestOne, requestTwo);

    allMocks = new Object[] {allowanceService, googleCalendarService, emailService, requestRepository, userRepository,
        absenceRepository};
  }

  @Test
  public void shouldFindAllByUserWithinDates() {
    // given
    given(userRepository.getOne(1L)).willReturn(mockUser);
    given(requestRepository.findAllByUser(mockUser)).willReturn(mockRequests);
    var startDate = LocalDate.of(2018, 11, 18);
    var endDate = LocalDate.of(2018, 11, 20);

    // when
    List<Request> requests = requestService.findAllByUserWithinDates(startDate, endDate, 1L);

    // then
    assertThat("Expect to list size be one ", requests.size(), is(1));
    assertThat("Expect to request date be 2018-11-20", requests.get(0).getDays().get(0).getDate(), is(endDate));
    verify(userRepository).getOne(anyLong());
    verify(requestRepository).findAllByUser(any());
    verifyNoMoreInteractions(allMocks);
  }

  @Test
  public void shouldFailToFindAnyRequestByUserWithinDates() {
    // given
    given(userRepository.getOne(1L)).willReturn(mockUser);
    given(requestRepository.findAllByUser(mockUser)).willReturn(mockRequests);
    var startDate = LocalDate.of(2018, 11, 18);
    var endDate = LocalDate.of(2018, 11, 18);

    // when
    List<Request> requests = requestService.findAllByUserWithinDates(startDate, endDate, 1L);

    // then
    assertThat("Expect to list size be one ", requests.isEmpty(), is(true));
    verify(userRepository).getOne(anyLong());
    verify(requestRepository).findAllByUser(any());
    verifyNoMoreInteractions(allMocks);
  }

  @Test
  public void shouldFindAllByUser() {
    // given
    given(userRepository.getOne(1L)).willReturn(mockUser);
    given(requestRepository.findAllByUser(mockUser)).willReturn(mockRequests);

    // when
    List<Request> requests = requestService.findAllByUser(1L);

    // then
    assertThat("Expect to list size be two", requests.size(), is(2));
    verify(userRepository).getOne(anyLong());
    verify(requestRepository).findAllByUser(any());
    verifyNoMoreInteractions(allMocks);
  }

  @Test(expected = EntityNotFoundException.class)
  public void shouldFailToFindAllByUser() {
    // given
    given(userRepository.getOne(2L)).willReturn(mockUser);
    given(requestRepository.findAllByUser(mockUser)).willThrow(new EntityNotFoundException());

    // when
    requestService.findAllByUser(2L);

    // then

  }

  @Test
  public void shouldFindAllByRequestStatus() {
    // given
    given(requestRepository.findAllByRequestStatus(RequestStatus.PENDING)).willReturn(mockRequests);

    // when
    List<Request> requests = requestService.findAllByRequestStatus(RequestStatus.PENDING);

    // then
    assertThat("Expect to list size be two", requests.size(), is(2));
    assertThat("Expect to request status be pending", requests.get(0).getRequestStatus(), is(RequestStatus.PENDING));
    verify(requestRepository).findAllByRequestStatus(any());
    verifyNoMoreInteractions(allMocks);
  }

  @Test(expected = EntityNotFoundException.class)
  public void shouldFailToFindAnyRequestByRequestStatus() {
    // given
    given(requestRepository.findAllByRequestStatus(RequestStatus.PENDING)).willThrow(new EntityNotFoundException());

    // when
    requestService.findAllByRequestStatus(RequestStatus.PENDING);

    // then

  }

  @Test
  public void shouldFindAllByAbsenceType() {
    // given
    given(requestRepository.findAllByAbsence_AbsenceType(AbsenceType.DEDUCTED_LEAVE)).willReturn(mockRequests);

    // when
    List<Request> requests = requestService.findAllByAbsenceType(AbsenceType.DEDUCTED_LEAVE);

    // then
    assertThat("Expect to list size be two", requests.size(), is(2));
    assertThat("Expect to request absence type be DECUTED LEAVE", requests.get(0).getAbsence().getAbsenceType(),
        is(AbsenceType.DEDUCTED_LEAVE));
    verify(requestRepository).findAllByAbsence_AbsenceType(any());
    verifyNoMoreInteractions(allMocks);
  }

  @Test(expected = EntityNotFoundException.class)
  public void shouldFailToFindAnyRequestByAbsenceType() {
    // given
    given(requestRepository.findAllByAbsence_AbsenceType(AbsenceType.DEDUCTED_LEAVE)).willThrow(
        new EntityNotFoundException());

    // when
    requestService.findAllByAbsenceType(AbsenceType.DEDUCTED_LEAVE);

    // then

  }

  @Test
  public void shouldGetRequestById() {
    // given
    given(requestRepository.getOne(1L)).willReturn(requestOne);

    // when
    Request request = requestService.getById(1L);

    // then
    assertThat("Expect to request is just for one day", request.getDays().size(), is(1));
    assertThat("Expect to request date is 2018-11-20", request.getDays().get(0).getDate(),
        is(LocalDate.of(2018, 11, 20)));
    verify(requestRepository).getOne(anyLong());
    verifyNoMoreInteractions(allMocks);
  }

  @Test(expected = EntityNotFoundException.class)
  public void shouldFailToFindRequestById() {
    // given
    given(requestRepository.getOne(2L)).willThrow(new EntityNotFoundException());

    // when
    requestService.getById(2L);

    // then

  }

  @Test
  public void shouldCreateRequest() {
    // given
    var user = EntityBuilder.user(EntityBuilder.team());
    var request = EntityBuilder.request(absenceTraining, List.of(dayOne));
    request.setUser(user);
    given(userRepository.getOne(1L)).willReturn(user);
    given(requestRepository.save(requestOne)).willReturn(request);

    // when
    Request savedRequest = requestService.create(requestOne);

    // then
    assertThat("Expect to request user email be aria.stark@gmail.com", savedRequest.getUser().getEmail(),
        is("aria.stark@gmail.com"));
    verify(userRepository).getOne(anyLong());
    verify(requestRepository).save(any());
    verify(googleCalendarService).handleCreatedRequest(any());
    verify(emailService).createEmailAndSendForApproval(any());
    verify(allowanceService).applyPendingRequest(any(), anyBoolean());
    verifyNoMoreInteractions(allMocks);
  }

  @Test(expected = EntityNotFoundException.class)
  public void shouldFailToFindUserForCreateRequest() {
    // given
    given(userRepository.getOne(1L)).willThrow(new EntityNotFoundException());

    // when
    requestService.create(requestOne);

    // then

  }

  @Test
  public void shouldHandleRequestStatusUpdateRejected() {
    // given
    var approver = EntityBuilder.approver();
    approver.setId(2L);

    var user = EntityBuilder.user(EntityBuilder.team());
    user.getTeam().getTeamApprovers().add(approver);

    var request = EntityBuilder.request(absenceAnnual, List.of(dayOne));
    request.setUser(mockUser);
    request.setRequestStatus(RequestStatus.REJECTED);

    var databaseRequest = EntityBuilder.request(absenceAnnual, List.of(dayOne));

    given(absenceRepository.getOne(1L)).willReturn(absenceAnnual);
    given(userRepository.getOne(1L)).willReturn(user);
    given(requestRepository.getOne(1L)).willReturn(databaseRequest);
    given(requestRepository.save(request)).willReturn(request);

    // when
    Request savedRequest = requestService.handleRequestStatusUpdate(request, approver);

    // then
    assertThat("Expect to saved request have status", savedRequest.getRequestStatus(), is(RequestStatus.REJECTED));
    assertNotNull("Expect to days exist", savedRequest.getDays());
    assertNotNull("Expect to user exist", savedRequest.getUser());
    assertNotNull("Expect to absence exist", savedRequest.getAbsence());
    verify(absenceRepository).getOne(anyLong());
    verify(userRepository).getOne(anyLong());
    verify(requestRepository, times(3)).getOne(anyLong());
    verify(allowanceService).applyPendingRequest(any(), anyBoolean());
    verify(requestRepository).save(any());
    verifyNoMoreInteractions(allMocks);
  }

  @Test
  public void shouldHandleRequestStatusUpdateCancellationPending() {
    // given
    var approver = EntityBuilder.approver();
    approver.setId(2L);

    var request = EntityBuilder.request(absenceAnnual, List.of(dayOne));
    request.setRequestStatus(RequestStatus.CANCELED);
    var databaseRequest = EntityBuilder.request(absenceAnnual, List.of(dayOne));
    databaseRequest.setRequestStatus(RequestStatus.APPROVED);
    var changedRequest = EntityBuilder.request(absenceAnnual, List.of(dayOne));
    changedRequest.setRequestStatus(RequestStatus.CANCELLATION_PENDING);

    given(absenceRepository.getOne(1L)).willReturn(absenceAnnual);
    given(userRepository.getOne(1L)).willReturn(request.getUser());
    given(requestRepository.getOne(1L)).willReturn(databaseRequest);
    given(requestRepository.save(changedRequest)).willReturn(changedRequest);

    // when
    Request savedRequest = requestService.handleRequestStatusUpdate(request, approver);

    // then
    assertThat("Expect to saved request have status", savedRequest.getRequestStatus(),
        is(RequestStatus.CANCELLATION_PENDING));
    verify(absenceRepository).getOne(anyLong());
    verify(userRepository).getOne(anyLong());
    verify(requestRepository, times(3)).getOne(anyLong());
    verify(emailService).createEmailAndSendForApproval(any());
    verify(requestRepository).save(any());
    verifyNoMoreInteractions(allMocks);
  }

  @Test(expected = EntityNotFoundException.class)
  public void shouldFailToHandleRequestStatusUpdate() {
    // given
    given(userRepository.getOne(1L)).willThrow(new EntityNotFoundException());

    // when
    requestService.handleRequestStatusUpdate(requestOne, mockUser);

    // then

  }

}