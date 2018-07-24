package eu.execom.hawaii.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.AbsenceType;
import eu.execom.hawaii.model.enumerations.RequestStatus;
import eu.execom.hawaii.repository.RequestRepository;
import eu.execom.hawaii.repository.UserRepository;

@Service
public class RequestService {

  private RequestRepository requestRepository;
  private UserRepository userRepository;
  private AllowanceService allowanceService;
  private GoogleCalendarService googleCalendarService;

  @Autowired
  public RequestService(RequestRepository requestRepository, UserRepository userRepository,
      AllowanceService allowanceService, GoogleCalendarService googleCalendarService) {
    this.requestRepository = requestRepository;
    this.userRepository = userRepository;
    this.allowanceService = allowanceService;
    this.googleCalendarService = googleCalendarService;
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
   * @param request the Request entity to be persisted.
   * @return a saved request with id.
   */
  public Request save(Request request) {
    return requestRepository.save(request);
  }

  /**
   * Saves changed request status.
   *
   * @param request to be persisted.
   * @return saved request.
   */
  public Request handleRequestStatusUpdate(Request request) throws Exception {
    checkIsApproved(request);

    return requestRepository.save(request);
  }

  private void checkIsApproved(Request request) throws Exception {
    if (RequestStatus.APPROVED.equals(request.getRequestStatus())) {
      allowanceService.applyRequest(request);
      googleCalendarService.insertRequestToCalendar(request);
    }
  }

}