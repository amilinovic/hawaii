package eu.execom.hawaii.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

  @Autowired
  public RequestService(RequestRepository requestRepository, UserRepository userRepository,
      AbsenceRepository absenceRepository, AllowanceService allowanceService,
      GoogleCalendarService googleCalendarService) {
    this.requestRepository = requestRepository;
    this.userRepository = userRepository;
    this.allowanceService = allowanceService;
    this.absenceRepository = absenceRepository;
    this.googleCalendarService = googleCalendarService;
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
    List<Request> requests = requestRepository.findAllByUserAndRequestStatusNot(user, RequestStatus.CANCELED);
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
   * Retrieves a list of requests by userId and without canceled requests from repository.
   *
   * @param userId the User id.
   * @return a list of all requests for given user.
   */
  public List<Request> findAllByUser(Long userId) {
    User user = userRepository.getOne(userId);
    return requestRepository.findAllByUserAndRequestStatusNot(user, RequestStatus.CANCELED);
  }

  /**
   * Retrieves a list of all requests by approverId from repository.
   *
   * @param approverId the User id.
   * @return a list of all requests for given user(approver).
   */
  public List<Request> findAllByApprover(Long approverId) {
    User approver = userRepository.getOne(approverId);
    return requestRepository.findAllByApprover(approver);
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
  public Request save(Request request) {
    request.getDays().forEach(day -> day.setRequest(request));


    User user = userRepository.getOne(request.getUser().getId());
    request.setUser(user);
    googleCalendarService.handleCreatedRequest(request);

    return requestRepository.save(request);
  }

  /**
   * Saves changed request status. If status is changed to APPROVED,
   * applies leave days from the request to the user's allowance
   * and creates an event in the user's Google calendar.
   *
   * @param request to be persisted.
   * @return saved request.
   */
  public Request handleRequestStatusUpdate(Request request) {
    Absence absence = absenceRepository.getOne(request.getAbsence().getId());
    request.setAbsence(absence);

    User user = userRepository.getOne(request.getUser().getId());
    request.setUser(user);

    if (shouldApplyRequest(request)) {
      applyRequest(request);
    }

    return requestRepository.save(request);
  }

  private boolean shouldApplyRequest(Request request) {
    return RequestStatus.APPROVED.equals(request.getRequestStatus()) || RequestStatus.CANCELED.equals(
        request.getRequestStatus());
  }

  private void applyRequest(Request request) {
    boolean requestCanceled = RequestStatus.CANCELED.equals(request.getRequestStatus());
    allowanceService.applyRequest(request, requestCanceled);
    googleCalendarService.handleRequestUpdate(request, requestCanceled);
  }

}