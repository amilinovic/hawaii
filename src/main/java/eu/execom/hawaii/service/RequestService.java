package eu.execom.hawaii.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.execom.hawaii.exceptions.NotAuthorizedApprovalExeception;
import eu.execom.hawaii.exceptions.RequestAlreadyCanceledException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RequestService {

  private static final String REQUESTS_CACHE = "requestsCache";

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

  public List<Request> findAllByDateRange(LocalDate startDate, LocalDate endDate) {
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
  @Cacheable(value = REQUESTS_CACHE, key = "#userId")
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
  @CacheEvict(value = REQUESTS_CACHE, key = "#request.user.id")
  @Transactional
  public Request create(Request request) {
    request.getDays().forEach(day -> day.setRequest(request));

    User user = userRepository.getOne(request.getUser().getId());
    request.setUser(user);
    LocalDateTime submissionTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    request.setSubmissionTime(submissionTime);
    googleCalendarService.handleCreatedRequest(request);

    var requests = findAllByUser(user.getId());
    List<Day> matchingDays = requests.stream()
                                     .map(Request::getDays)
                                     .flatMap(Collection::stream)
                                     .filter(isRequestDaysMatch(request))
                                     .collect(Collectors.toList());

    if (!matchingDays.isEmpty()) {
      log.error("Request for days: {} overlaps with existing requests.",
          matchingDays.stream().map(day -> day.getDate().toString()).collect(Collectors.joining(", ")));
      throw new EntityExistsException();
    }

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

  private Predicate<Day> isRequestDaysMatch(Request request) {
    return day -> request.getDays().stream().anyMatch(requestDay -> requestDay.getDate().equals(day.getDate()));
  }

  /**
   * Saves changed request status. If status is changed to APPROVED/CANCELED/REJECTED,
   * applies leave days from the request to the user's allowance
   * and creates an event in the user's Google calendar.
   *
   * @param request to be persisted.
   * @return saved request.
   */
  @CacheEvict(value = REQUESTS_CACHE, key = "#request.user.id")
  @Transactional
  public Request handleRequestStatusUpdate(Request request, User authUser) {
    Absence absence = absenceRepository.getOne(request.getAbsence().getId());
    request.setAbsence(absence);

    User user = userRepository.getOne(request.getUser().getId());
    request.setUser(user);

    var existingRequest = getById(request.getId());
    boolean userIsRequestApprover = isUserRequestApprover(authUser, user);
    boolean requestIsApproved = existingRequest.isApproved();
    boolean requestHasPendingCancellation = existingRequest.isCancellationPending();
    boolean requestIsPending = existingRequest.isPending();
    boolean requestIsCanceled = existingRequest.isCanceled();

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
          throw new RequestAlreadyCanceledException();
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

  private void applyRequest(Request request, boolean requestCanceled) {
    allowanceService.applyRequest(request, requestCanceled);
    emailService.createStatusNotificationEmailAndSend(request);
    if (!requestCanceled) {
      emailService.createAnnualEmailForTeammatesAndSend(request);
    }
    googleCalendarService.handleRequestUpdate(request, requestCanceled);
  }

}