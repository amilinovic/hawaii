package eu.execom.hawaii.service;

import eu.execom.hawaii.exceptions.NotAuthorizedApprovalException;
import eu.execom.hawaii.exceptions.RequestAlreadyCanceledException;
import eu.execom.hawaii.model.Day;
import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.Team;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.audit.RequestAudit;
import eu.execom.hawaii.model.enumerations.AbsenceType;
import eu.execom.hawaii.model.enumerations.Duration;
import eu.execom.hawaii.model.enumerations.OperationPerformed;
import eu.execom.hawaii.model.enumerations.RequestStatus;
import eu.execom.hawaii.repository.DayRepository;
import eu.execom.hawaii.repository.RequestRepository;
import eu.execom.hawaii.repository.TeamRepository;
import eu.execom.hawaii.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RequestService {

  private static final String REQUESTS_CACHE = "requestsCache";
  private static final String APPROVE = "approve";
  private static final String CANCEL = "cancel";
  private static final String REJECT = "reject";

  private RequestRepository requestRepository;
  private UserRepository userRepository;
  private DayRepository dayRepository;
  private TeamRepository teamRepository;
  private AllowanceService allowanceService;
  private GoogleCalendarService googleCalendarService;
  private EmailService emailService;
  private SendNotificationsService sendNotificationsService;
  private AuditInformationService auditInformationService;

  @Autowired
  public RequestService(RequestRepository requestRepository, UserRepository userRepository, DayRepository dayRepository,
      TeamRepository teamRepository, AllowanceService allowanceService, GoogleCalendarService googleCalendarService,
      EmailService emailService, SendNotificationsService sendNotificationsService,
      AuditInformationService auditInformationService) {
    this.requestRepository = requestRepository;
    this.userRepository = userRepository;
    this.dayRepository = dayRepository;
    this.teamRepository = teamRepository;
    this.allowanceService = allowanceService;
    this.googleCalendarService = googleCalendarService;
    this.emailService = emailService;
    this.sendNotificationsService = sendNotificationsService;
    this.auditInformationService = auditInformationService;
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
   * @param endDate to date.
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
   * Retrieves a list of requests by userId for given year from repository.
   *
   * @param userId the userId
   * @param date requested date
   * @return a list of requests for user in given year.
   */
  public List<Request> findAllByUserForYear(Long userId, LocalDate date) {
    var startDate = date.withDayOfYear(1);
    var endDate = date.withDayOfYear(date.lengthOfYear());

    return findAllByUserWithinDates(startDate, endDate, userId);
  }

  /**
   * Retrieves a list of requests for all users from requested team and requested month.
   *
   * @param teamId the Team id.
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
   * Saves the provided Request to repository.
   * Makes audit of that save.
   *
   * @param request the Request entity to be persisted.
   * @param modifiedByUser user that made changes to that Request entity.
   * @return saved Request.
   */
  @Transactional
  public Request save(Request request, User modifiedByUser) {
    saveAuditInformation(OperationPerformed.CREATE, modifiedByUser, request, null);
    return requestRepository.save(request);
  }

  /**
   * Saves the provided Request to repository.
   * Makes audit of that save.
   *
   * @param request the Request entity to be persisted.
   * @param modifiedByUser user that made changes to that Request entity.
   * @return saved Request.
   */
  @Transactional
  public Request update(Request request, User modifiedByUser) {
    var previousRequestState = RequestAudit.fromRequest(getById(request.getId()));
    saveAuditInformation(OperationPerformed.UPDATE, modifiedByUser, request, previousRequestState);

    return requestRepository.save(request);
  }

  /**
   * Save the provided request to repository, with setting initial status
   * of request depending of absence type SICKNESS or any other.
   * Also applies leave days from request to pending field on user's allowance.
   *
   * @param newRequest the Request entity to be persisted.
   * @return a saved request with id.
   */
  @CacheEvict(value = REQUESTS_CACHE, key = "#newRequest.user.id")
  @Transactional
  public Request create(Request newRequest, User authUser) {
    newRequest.getDays().forEach(day -> day.setRequest(newRequest));

    User user = userRepository.getOne(newRequest.getUser().getId());
    newRequest.setUser(user);
    LocalDateTime submissionTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    newRequest.setSubmissionTime(submissionTime);
    newRequest.setCurrentlyApprovedBy(new ArrayList<>());
    googleCalendarService.handleCreatedRequest(newRequest);

    var requests = findAllByUser(user.getId());
    List<Day> matchingDays = requests.stream()
                                     .map(Request::getDays)
                                     .flatMap(Collection::stream)
                                     .filter(isRequestDaysMatch(newRequest))
                                     .filter(isDayRequestApprovedOrPending())
                                     .collect(Collectors.toList());

    if (!matchingDays.isEmpty()) {
      logAndThrowEntityExistsException(matchingDays);
    }
    if (AbsenceType.SICKNESS.equals(newRequest.getAbsence().getAbsenceType())) {
      newRequest.setRequestStatus(RequestStatus.APPROVED);
      allowanceService.applyRequest(newRequest, false);
      emailService.sendSicknessEmailNotification(newRequest);
      save(newRequest, authUser);
    } else {
      newRequest.setRequestStatus(RequestStatus.PENDING);
      allowanceService.applyPendingRequest(newRequest, false);
      emailService.createEmailAndSendForApproval(newRequest);
      save(newRequest, authUser);
      sendNotificationsService.sendNotificationToApproversAboutSubmittedRequest(newRequest);
    }
    return newRequest;
  }

  private void logAndThrowEntityExistsException(List<Day> matchingDays) {
    log.error("Request for days: '{}' overlaps with existing requests.",
        matchingDays.stream().map(day -> day.getDate().toString()).collect(Collectors.joining(", ")));
    throw new EntityExistsException("New request overlaps with existing one.");
  }

  private Predicate<Day> isRequestDaysMatch(Request newRequest) {
    return day -> newRequest.getDays()
                            .stream()
                            .anyMatch(newRequestDay -> newRequestDay.getDate().equals(day.getDate()) && (
                                newRequestDay.getDuration().equals(day.getDuration()) || Duration.FULL_DAY.equals(
                                    newRequestDay.getDuration()) || Duration.FULL_DAY.equals(day.getDuration())));
  }

  private Predicate<Day> isDayRequestApprovedOrPending() {
    return day -> day.getRequest().isApproved() || day.getRequest().isPending() || day.getRequest()
                                                                                      .isCancellationPending();
  }

  /**
   * Saves changed request status. If status is changed to APPROVED/CANCELED/REJECTED,
   * applies leave days from the request to the user's allowance,
   * creates an event in the user's Google calendar
   * and sends notification to user who made request, WHEN the request is handled by approver
   *
   * @param request to be persisted.
   * @return saved request.
   */
  @CacheEvict(value = REQUESTS_CACHE, key = "#request.user.id")
  @Transactional
  public Request handleRequestStatusUpdate(Request request, User authUser) {

    User user = userRepository.getOne(request.getUser().getId());
    request.setUser(user);

    var existingRequest = getById(request.getId());
    boolean userIsRequestApprover = isUserRequestApprover(authUser, user);
    boolean requestIsApproved = existingRequest.isApproved();
    boolean requestHasPendingCancellation = existingRequest.isCancellationPending();
    boolean requestIsPending = existingRequest.isPending();
    boolean requestIsCanceled = existingRequest.isCanceled();
    boolean userIsAuthUser = user.getId().equals(authUser.getId());

    switch (request.getRequestStatus()) {
      case APPROVED:
        if (!userIsRequestApprover) {
          logAndThrowNotAuthorizedApprovalException(user, APPROVE);
        }
        if (request.getAbsence().isBonusDays()) {
          handleBonusRequestApproval(request, authUser);
          if (request.isPending()) {
            break;
          }
        }
        setApprovedBy(request,authUser);
        allowanceService.applyPendingRequest(request, true);
        applyRequest(request, false);
        sendNotificationsService.sendNotificationForRequestedLeave(request.getRequestStatus(), user);
        break;
      case CANCELED:
        if (requestIsCanceled) {
          logAndThrowRequestAlreadyCanceledException(user);
        } else if (userIsRequestApprover && (requestIsApproved || requestHasPendingCancellation)) {
          applyRequest(request, true);
          sendNotificationsService.sendNotificationForRequestedLeave(request.getRequestStatus(), user);
        } else if (!userIsRequestApprover && userIsAuthUser && requestIsApproved) {
          request.setRequestStatus(RequestStatus.CANCELLATION_PENDING);
          emailService.createEmailAndSendForApproval(request);
          sendNotificationsService.sendNotificationToApproversAboutSubmittedRequest(request);
        } else if (requestIsPending && userIsAuthUser) {
          allowanceService.applyPendingRequest(request, true);
        } else {
          logAndThrowNotAuthorizedApprovalException(user, CANCEL);
        }
        break;
      case REJECTED:
        if (!userIsRequestApprover) {
          logAndThrowNotAuthorizedApprovalException(user, REJECT);
        }
        allowanceService.applyPendingRequest(request, true);
        sendNotificationsService.sendNotificationForRequestedLeave(request.getRequestStatus(), user);
        break;
      default:
        throw new IllegalArgumentException("Unsupported request status: " + request.getRequestStatus());
    }

    return update(request, authUser);
  }

  private void logAndThrowRequestAlreadyCanceledException(User user) {
    log.error("Request by user: '{}', is already canceled.", user.getEmail());
    throw new RequestAlreadyCanceledException();
  }

  private void logAndThrowNotAuthorizedApprovalException(User user, String handleAction) {
    log.error("Approver not authorized to '{}' this request for user with email: '{}'", handleAction, user.getEmail());
    throw new NotAuthorizedApprovalException(handleAction);
  }

  private void saveAuditInformation(OperationPerformed operationPerformed, User modifiedByUser, Request request,
      RequestAudit previousRequestState) {
    var currentRequestState = RequestAudit.fromRequest(request);

    auditInformationService.saveAudit(operationPerformed, modifiedByUser, request.getUser(), previousRequestState,
        currentRequestState);
  }

  private void handleBonusRequestApproval(Request request, User approver) {
    setCurrentlyApprovedBy(approver, request);
    int neededApprovals = request.getUser().getTeam().getTeamApprovers().size();
    if (request.getCurrentlyApprovedBy().size() != neededApprovals) {
      request.setRequestStatus(RequestStatus.PENDING);
    }
  }
  private void setApprovedBy(Request request, User user){
    if(!request.getAbsence().isBonusDays()) {
      request.setCurrentlyApprovedBy(List.of(user));
    }
  }

  private void setCurrentlyApprovedBy(User approver, Request request) {
    List<User> currentlyApprovedBy = request.getCurrentlyApprovedBy();

    if (!isRequestAlreadyApprovedByApprover(approver, request)) {
      currentlyApprovedBy.add(approver);
      request.setCurrentlyApprovedBy(currentlyApprovedBy);
      requestRepository.save(request);
    }
  }

  private boolean isRequestAlreadyApprovedByApprover(User approver, Request request) {
    return request.getCurrentlyApprovedBy()
                  .stream()
                  .anyMatch(teamApprover -> teamApprover.getId().equals(approver.getId()));
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
      sendEmailToTeammatesAndNotifiers(request);
    }
    googleCalendarService.handleRequestUpdate(request, requestCanceled);
  }

  private void sendEmailToTeammatesAndNotifiers(Request request) {
    if (AbsenceType.BONUS_DAYS.equals(request.getAbsence().getAbsenceType())) {
      emailService.sendBonusRequestEmailNotification(request);
    } else {
      emailService.sendAnnualRequestEmailNotification(request);
    }
  }
}