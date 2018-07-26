package eu.execom.hawaii.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.AbsenceType;
import eu.execom.hawaii.model.enumerations.RequestStatus;
import eu.execom.hawaii.repository.DayRepository;
import eu.execom.hawaii.repository.RequestRepository;
import eu.execom.hawaii.repository.UserRepository;

@Service
public class RequestService {

  private RequestRepository requestRepository;
  private UserRepository userRepository;
  private DayRepository dayRepository;
  private AllowanceService allowanceService;

  @Autowired
  public RequestService(RequestRepository requestRepository, UserRepository userRepository, DayRepository dayRepository,
      AllowanceService allowanceService) {
    this.requestRepository = requestRepository;
    this.userRepository = userRepository;
    this.dayRepository = dayRepository;
    this.allowanceService = allowanceService;
  }

  /**
   * Retrieves a list of request by given dates, ordered by latest.
   *
   * @param startDate from date.
   * @param endDate   to date.
   * @return a list of requests.
   */
  public List<Request> findAllByDates(LocalDate startDate, LocalDate endDate) {
    List<Request> requests = requestRepository.findAllByRequestStatusNot(RequestStatus.CANCELED);
    return requests.stream()
                   .filter(isBetween(startDate, endDate))
                   .sorted(Comparator.comparing((Request req) -> req.getDays().get(0).getDate()).reversed())
                   .collect(Collectors.toList());
  }

  private Predicate<Request> isBetween(LocalDate startDate, LocalDate endDate) {
    return request -> checkDates(request, startDate, endDate);
  }

  private boolean checkDates(Request request, LocalDate startDate, LocalDate endDate) {
    LocalDate requestDate = request.getDays().get(0).getDate();
    return (requestDate.isAfter(startDate) || requestDate.isEqual(startDate)) && (requestDate.isBefore(endDate)
        || requestDate.isEqual(endDate));
  }

  /**
   * Retrieves a list of all requests by userId from repository.
   *
   * @param userId the User id.
   * @return a list of all requests for given user.
   */
  public List<Request> findAllByUser(Long userId) {
    User user = userRepository.getOne(userId);
    return requestRepository.findAllByUser(user);
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
   * @param mappedRequest the Request entity to be persisted.
   * @return a saved request with id.
   */
  public Request save(Request mappedRequest) {
    var request = requestRepository.save(mappedRequest);
    request.getDays().forEach(day -> day.setRequest(request));
    var days = dayRepository.saveAll(request.getDays());
    request.setDays(days);
    return request;
  }

  /**
   * Saves changed request status.
   *
   * @param request to be persisted.
   * @return saved request.
   */
  public Request handleRequestStatusUpdate(Request request) {
    checkIsApproved(request);

    return requestRepository.save(request);
  }

  private void checkIsApproved(Request request) {
    if (RequestStatus.APPROVED.equals(request.getRequestStatus())) {
      allowanceService.applyRequest(request);
    }
  }

}