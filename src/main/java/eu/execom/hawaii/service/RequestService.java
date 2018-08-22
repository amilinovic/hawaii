package eu.execom.hawaii.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.execom.hawaii.exceptions.NotAuthorizedApprovalExeception;
import eu.execom.hawaii.model.Absence;
import eu.execom.hawaii.model.Day;
import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.AbsenceType;
import eu.execom.hawaii.model.enumerations.RequestStatus;
import eu.execom.hawaii.repository.AbsenceRepository;
import eu.execom.hawaii.repository.RequestRepository;
import eu.execom.hawaii.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RequestService {

  private RequestRepository requestRepository;
  private UserRepository userRepository;
  private AbsenceRepository absenceRepository;
  private AllowanceService allowanceService;
  private GoogleCalendarService googleCalendarService;
  private EmailService emailService;

  @Autowired
  public RequestService(RequestRepository requestRepository, UserRepository userRepository,
      AbsenceRepository absenceRepository, AllowanceService allowanceService,
      GoogleCalendarService googleCalendarService, EmailService emailService) {
    this.requestRepository = requestRepository;
    this.userRepository = userRepository;
    this.allowanceService = allowanceService;
    this.absenceRepository = absenceRepository;
    this.googleCalendarService = googleCalendarService;
    this.emailService = emailService;
  }

  /**
   * Retrieves a list of request by given dates, ordered by latest.
   *
   * @param startDate from date.
   * @param endDate   to date.
   * @return a list of requests.
   */
  public List<Request> findAllByUserWithinDates(LocalDate startDate, LocalDate endDate, Long userId) {
    User user = userRepository.getOne(userId);
    List<Request> requests = requestRepository.findAllByUser(user);
    return requests.stream()
                   .filter(isBetween(startDate, endDate))
                   .sorted(Comparator.comparing((Request req) -> req.getDays().get(0).getDate()).reversed())
                   .collect(Collectors.toList());
  }

  private Predicate<Request> isBetween(LocalDate startDate, LocalDate endDate) {
    return request -> checkAnyDayWithinDates(request.getDays(), startDate, endDate);
  }

  private boolean checkAnyDayWithinDates(List<Day> days, LocalDate startDate, LocalDate endDate) {
    return days.stream()
               .anyMatch(day -> (day.getDate().isAfter(startDate) || day.getDate().isEqual(startDate)) && (
                   day.getDate().isBefore(endDate) || day.getDate().isEqual(endDate)));
  }

  /**
   * Retrieves a list of requests by userId from repository.
   *
   * @param userId the User id.
   * @return a list of all requests for given user.
   */
  public List<Request> findAllByUser(Long userId) {
    User user = userRepository.getOne(userId);
    return requestRepository.findAllByUser(user);
  }

  /**
   * Retrieves a list of all requests by status of request from repository.
   *
   * @param requestStatus status of request.
   * @return a list of all requests for given request status.
   */
  public List<Request> findAllByRequestStatus(RequestStatus requestStatus) {
    return requestRepository.findAllByRequestStatus(requestStatus);
  }

  /**
   * Retrieves a list of all requests by absence type from repository.
   *
   * @param absenceType a type of Absence.
   * @return a list of all requests for given absence type.
   */
  public List<Request> findAllByAbsenceType(AbsenceType absenceType) {
    return requestRepository.findAllByAbsence_AbsenceType(absenceType);
  }

  /**
   * Retrieves a Request with a specific id.
   *
   * @param id Request id.
   * @return Request for given id if exist.
   */
  public Request getById(Long id) {
    return requestRepository.getOne(id);
  }

  /**
   * Save the provided request to repository.
   *
   * @param request the Request entity to be persisted.
   * @return a saved request with id.
   */
  public Request create(Request request) {
    request.getDays().forEach(day -> day.setRequest(request));

    User user = userRepository.getOne(request.getUser().getId());
    request.setUser(user);
    googleCalendarService.handleCreatedRequest(request);

    if (AbsenceType.SICKNESS.equals(request.getAbsence().getAbsenceType())) {
      request.setRequestStatus(RequestStatus.APPROVED);
      emailService.createSicknessEmailForTeammatesAndSend(request);
    } else {
      request.setRequestStatus(RequestStatus.PENDING);
      emailService.createEmailAndSendForApproval(request);
    }

    return requestRepository.save(request);
  }

  /**
   * Saves changed request status. If status is changed to APPROVED/CANCELED,
   * applies leave days from the request to the user's allowance
   * and creates an event in the user's Google calendar.
   *
   * @param request to be persisted.
   * @return saved request.
   */
  public Request handleRequestStatusUpdate(Request request, User authUser) {
    Absence absence = absenceRepository.getOne(request.getAbsence().getId());
    request.setAbsence(absence);

    User user = userRepository.getOne(request.getUser().getId());
    request.setUser(user);

    switch (request.getRequestStatus()) {
      case APPROVED:
        if (!isUserRequestApprover(authUser, user)) {
          log.error("Approver not authorized to approve this request for user with email: {}", user.getEmail());
          throw new NotAuthorizedApprovalExeception();
        }
        applyRequest(request, false);
        break;
      case CANCELED:
        if (isUserRequestApprover(authUser, user) && (isApproved(request.getId())
            || isCancellationPending(request.getId()))) {
          applyRequest(request, true);
        } else if (!isUserRequestApprover(authUser, user) && isApproved(request.getId())) {
          request.setRequestStatus(RequestStatus.CANCELLATION_PENDING);
          emailService.createEmailAndSendForApproval(request);
        }
        break;
      default:
        log.info("Request with status {} will be saved without changing allowance for user with email {}",
            request.getRequestStatus(), request.getUser().getEmail());
    }

    return requestRepository.save(request);
  }

  private boolean isUserRequestApprover(User approver, User requestUser) {
    return requestUser.getTeam()
                      .getTeamApprovers()
                      .stream()
                      .anyMatch(teamApprover -> teamApprover.getId().equals(approver.getId()));
  }

  private boolean isApproved(Long requestId) {
    var existingRequest = getById(requestId);

    return RequestStatus.APPROVED.equals(existingRequest.getRequestStatus());
  }

  private boolean isCancellationPending(Long requestId) {
    var existingRequest = getById(requestId);

    return RequestStatus.CANCELLATION_PENDING.equals(existingRequest.getRequestStatus());
  }

  private void applyRequest(Request request, boolean requestCanceled) {
    allowanceService.applyRequest(request, requestCanceled);
    emailService.createStatusNotificationEmailAndSend(request);
    emailService.createAnnualEmailForTeammatesAndSend(request);
    googleCalendarService.handleRequestUpdate(request, requestCanceled);
  }

}