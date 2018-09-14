package eu.execom.hawaii.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.execom.hawaii.exceptions.NotAuthorizedApprovalExeception;
import eu.execom.hawaii.model.Absence;
import eu.execom.hawaii.model.Day;
import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.Team;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.AbsenceType;
import eu.execom.hawaii.model.enumerations.RequestStatus;
import eu.execom.hawaii.repository.AbsenceRepository;
import eu.execom.hawaii.repository.DayRepository;
import eu.execom.hawaii.repository.RequestRepository;
import eu.execom.hawaii.repository.TeamRepository;
import eu.execom.hawaii.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RequestService {

  private RequestRepository requestRepository;
  private UserRepository userRepository;
  private AbsenceRepository absenceRepository;
  private DayRepository dayRepository;
  private TeamRepository teamRepository;
  private AllowanceService allowanceService;
  private GoogleCalendarService googleCalendarService;
  private EmailService emailService;

  @Autowired
  public RequestService(RequestRepository requestRepository, UserRepository userRepository,
      AbsenceRepository absenceRepository, DayRepository dayRepository, TeamRepository teamRepository,
      AllowanceService allowanceService, GoogleCalendarService googleCalendarService, EmailService emailService) {
    this.requestRepository = requestRepository;
    this.userRepository = userRepository;
    this.dayRepository = dayRepository;
    this.teamRepository = teamRepository;
    this.allowanceService = allowanceService;
    this.absenceRepository = absenceRepository;
    this.googleCalendarService = googleCalendarService;
    this.emailService = emailService;
  }

  public List<Request> findAllByMonth(LocalDate startDate, LocalDate endDate) {
    List<Request> requests = requestRepository.findAll();
    return requests.stream()
                   .filter(isBetween(startDate, endDate))
                   .sorted(Comparator.comparing((Request r) -> r.getDays().get(0).getDate()).reversed())
                   .collect(Collectors.toList());
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
   * Retrieves a list of requests for all users from requested team and requested month.
   *
   * @param teamId        the Team id.
   * @param requestedDate the LocalDate.
   * @return a list of all requests for given team.
   */
  public List<Request> findAllByTeamByMonthOfYear(Long teamId, LocalDate requestedDate) {
    var startDate = requestedDate.withDayOfMonth(1);
    var endDate = requestedDate.withDayOfMonth(requestedDate.lengthOfMonth());
    Team team = teamRepository.getOne(teamId);
    List<User> teamUsers = team.getUsers();
    return teamUsers.stream()
                    .map(user -> findAllByUserWithinDates(startDate, endDate, user.getId()))
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
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
   * Retrieves a first and last requests year.
   *
   * @return a Map of first and last year.
   */
  public Map<String, Integer> getFirstAndLastRequestsYear() {
    Map<String, Integer> firstAndLastDate = new LinkedHashMap<>();
    var firstDayRequest = dayRepository.findFirstByOrderByDateAsc();
    var lastDayRequest = dayRepository.findFirstByOrderByDateDesc();
    var firstYear = firstDayRequest.getDate().getYear();
    var lastYear = lastDayRequest.getDate().getYear();

    firstAndLastDate.put("first", firstYear);
    firstAndLastDate.put("last", lastYear);

    return firstAndLastDate;
  }

  /**
   * Save the provided request to repository, with setting initial status
   * of request depending of absence type SICKNESS or any other.
   * Also applies leave days from request to pending field on user's allowance.
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
      allowanceService.applyRequest(request, false);
      emailService.createSicknessEmailForTeammatesAndSend(request);
    } else {
      request.setRequestStatus(RequestStatus.PENDING);
      allowanceService.applyPendingRequest(request, false);
      emailService.createEmailAndSendForApproval(request);
    }

    return requestRepository.save(request);
  }

  /**
   * Saves changed request status. If status is changed to APPROVED/CANCELED/REJECTED,
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

    var existingRequest = getById(request.getId());
    boolean userIsRequestApprover = isUserRequestApprover(authUser, user);
    boolean requestIsApproved = isApproved(existingRequest);
    boolean requestHasPendingCancellation = isCancellationPending(existingRequest);
    boolean requestIsPending = isPending(existingRequest);
    boolean requestIsCanceled = isCanceled(existingRequest);

    switch (request.getRequestStatus()) {
      case APPROVED:
        if (!userIsRequestApprover) {
          log.error("Approver not authorized to approve this request for user with email: {}", user.getEmail());
          throw new NotAuthorizedApprovalExeception();
        }
        allowanceService.applyPendingRequest(request, true);
        applyRequest(request, false);
        break;
      case CANCELED:
        if (requestIsCanceled) {
          log.error("Request by user: {}, is already canceled.", user.getEmail());
          throw new EntityExistsException();
        } else if (userIsRequestApprover && (requestIsApproved || requestHasPendingCancellation)) {
          applyRequest(request, true);
        } else if (!userIsRequestApprover && requestIsApproved) {
          request.setRequestStatus(RequestStatus.CANCELLATION_PENDING);
          emailService.createEmailAndSendForApproval(request);
        } else if (!userIsRequestApprover && requestHasPendingCancellation) {
          log.error("User not authorized to cancel this request for user with email: {}", user.getEmail());
          throw new NotAuthorizedApprovalExeception();
        } else if (requestIsPending) {
          allowanceService.applyPendingRequest(request, true);
        }
        break;
      case REJECTED:
        if (!userIsRequestApprover) {
          log.error("Approver not authorized to reject this request for user with email: {}", user.getEmail());
          throw new NotAuthorizedApprovalExeception();
        }
        allowanceService.applyPendingRequest(request, true);
        break;
      default:
        throw new IllegalArgumentException("Unsupported request status: " + request.getRequestStatus());
    }

    return requestRepository.save(request);
  }

  private boolean isUserRequestApprover(User approver, User requestUser) {
    return requestUser.getTeam()
                      .getTeamApprovers()
                      .stream()
                      .anyMatch(teamApprover -> teamApprover.getId().equals(approver.getId()));
  }

  private boolean isApproved(Request existingRequest) {
    return RequestStatus.APPROVED.equals(existingRequest.getRequestStatus());
  }

  private boolean isCancellationPending(Request existingRequest) {
    return RequestStatus.CANCELLATION_PENDING.equals(existingRequest.getRequestStatus());
  }

  private boolean isPending(Request existingRequest) {
    return RequestStatus.PENDING.equals(existingRequest.getRequestStatus());
  }

  private boolean isCanceled(Request existingRequest) {
    return RequestStatus.CANCELED.equals(existingRequest.getRequestStatus());
  }

  private void applyRequest(Request request, boolean requestCanceled) {
    allowanceService.applyRequest(request, requestCanceled);
    emailService.createStatusNotificationEmailAndSend(request);
    if (!requestCanceled) {
      emailService.createAnnualEmailForTeammatesAndSend(request);
    }
    googleCalendarService.handleRequestUpdate(request, requestCanceled);
  }

}