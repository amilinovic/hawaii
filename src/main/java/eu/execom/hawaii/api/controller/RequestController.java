package eu.execom.hawaii.api.controller;

import eu.execom.hawaii.dto.RequestDto;
import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.Team;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.AbsenceType;
import eu.execom.hawaii.model.enumerations.AuditedEntity;
import eu.execom.hawaii.model.enumerations.OperationPerformed;
import eu.execom.hawaii.model.enumerations.RequestStatus;
import eu.execom.hawaii.service.AuditInformationService;
import eu.execom.hawaii.service.RequestService;
import eu.execom.hawaii.service.SendNotificationsService;
import eu.execom.hawaii.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

  private static final ModelMapper MAPPER = new ModelMapper();

  private RequestService requestService;
  private UserService userService;
  private SendNotificationsService sendNotificationsService;
  private AuditInformationService auditInformationService;

  @Autowired
  public RequestController(RequestService requestService, UserService userService,
      SendNotificationsService sendNotificationsService, AuditInformationService auditInformationService) {
    this.requestService = requestService;
    this.userService = userService;
    this.sendNotificationsService = sendNotificationsService;
    this.auditInformationService = auditInformationService;
  }

  @GetMapping("/month")
  public ResponseEntity<List<RequestDto>> getAllRequestsByMonthOfYear(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    var startDate = date.withDayOfMonth(1);
    var endDate = date.withDayOfMonth(date.lengthOfMonth());
    List<Request> requests = requestService.findAllByDateRange(startDate, endDate);
    var requestDtos = requests.stream().map(RequestDto::new).collect(Collectors.toList());

    return new ResponseEntity<>(requestDtos, HttpStatus.OK);
  }

  @GetMapping("/approval")
  public ResponseEntity<List<RequestDto>> getAllRequestsForApproval(@ApiIgnore @AuthenticationPrincipal User authUser) {
    var loggedUser = userService.findByEmail(authUser.getEmail());
    List<Request> requests = getRequestsForApprover(loggedUser.getApproverTeams());
    var requestDtos = requests.stream().map(RequestDto::new).collect(Collectors.toList());

    return new ResponseEntity<>(requestDtos, HttpStatus.OK);
  }

  private List<Request> getRequestsForApprover(List<Team> approverTeams) {
    return approverTeams.stream()
                        .map(Team::getUsers)
                        .flatMap(List::stream)
                        .map(User::getRequests)
                        .flatMap(List::stream)
                        .filter(pendingRequests())
                        .collect(Collectors.toList());
  }

  private Predicate<Request> pendingRequests() {
    return request -> RequestStatus.PENDING.equals(request.getRequestStatus())
        || RequestStatus.CANCELLATION_PENDING.equals(request.getRequestStatus());
  }

  @GetMapping("/years/range")
  public ResponseEntity<Map<String, Integer>> getFirstAndLastRequestsYear() {
    var firstAndLastRequestDates = requestService.getFirstAndLastRequestsYear();
    return new ResponseEntity<>(firstAndLastRequestDates, HttpStatus.OK);
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<List<RequestDto>> getRequestsByUserId(@PathVariable Long id) {
    var requests = requestService.findAllByUser(id);
    var requestDtos = requests.stream().map(RequestDto::new).collect(Collectors.toList());

    return new ResponseEntity<>(requestDtos, HttpStatus.OK);
  }

  @GetMapping("/user")
  public ResponseEntity<List<RequestDto>> geUserRequests(@ApiIgnore @AuthenticationPrincipal User authUser) {
    var requests = requestService.findAllByUser(authUser.getId());
    var requestDtos = requests.stream().map(RequestDto::new).collect(Collectors.toList());

    return new ResponseEntity<>(requestDtos, HttpStatus.OK);
  }

  @GetMapping("/user/month")
  public ResponseEntity<List<RequestDto>> getUserRequestsByMonth(@ApiIgnore @AuthenticationPrincipal User authUser,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    var startDate = date.withDayOfMonth(1);
    var endDate = date.withDayOfMonth(date.lengthOfMonth());
    var requests = requestService.findAllByUserWithinDates(startDate, endDate, authUser.getId());
    var requestDtos = requests.stream().map(RequestDto::new).collect(Collectors.toList());

    return new ResponseEntity<>(requestDtos, HttpStatus.OK);
  }

  @GetMapping("/user/{id}/dates")
  public ResponseEntity<List<RequestDto>> getRequestsByUserByDates(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate, @PathVariable Long id) {
    var requests = requestService.findAllByUserWithinDates(startDate, endDate, id);
    var requestDtos = requests.stream().map(RequestDto::new).collect(Collectors.toList());

    return new ResponseEntity<>(requestDtos, HttpStatus.OK);
  }

  @GetMapping("/team/{teamId}/month")
  public ResponseEntity<List<RequestDto>> getTeamRequestsByMonth(@PathVariable Long teamId,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    var requests = requestService.findAllByTeamByMonthOfYear(teamId, date);
    var requestDtos = requests.stream().map(RequestDto::new).collect(Collectors.toList());

    return new ResponseEntity<>(requestDtos, HttpStatus.OK);
  }

  @GetMapping("/requeststatus/{status}")
  public ResponseEntity<List<RequestDto>> getRequestsByRequestStatus(@PathVariable RequestStatus status) {
    var requests = requestService.findAllByRequestStatus(status);
    var requestDtos = requests.stream().map(RequestDto::new).collect(Collectors.toList());

    return new ResponseEntity<>(requestDtos, HttpStatus.OK);
  }

  @GetMapping("/absencetype/{type}")
  public ResponseEntity<List<RequestDto>> getRequestsByAbsenceType(@PathVariable AbsenceType type) {
    var requests = requestService.findAllByAbsenceType(type);
    var requestDtos = requests.stream().map(RequestDto::new).collect(Collectors.toList());

    return new ResponseEntity<>(requestDtos, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<RequestDto> getById(@PathVariable Long id) {
    var request = requestService.getById(id);

    return new ResponseEntity<>(new RequestDto(request), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<RequestDto> createRequest(@ApiIgnore @AuthenticationPrincipal User authUser,
      @RequestBody RequestDto requestDto) {
    var request = MAPPER.map(requestDto, Request.class);
    request.setUser(authUser);
    request = requestService.create(request);
    sendAuditInformation(OperationPerformed.CREATE, authUser, request.getUser(), null, requestDto);

    return new ResponseEntity<>(new RequestDto(request), HttpStatus.OK);
  }

  @PutMapping
  public ResponseEntity<RequestDto> handleRequestStatus(@ApiIgnore @AuthenticationPrincipal User authUser,
      @RequestBody RequestDto requestDto) {
    var oldRequest = requestService.getById(requestDto.getId());
    var request = MAPPER.map(requestDto, Request.class);
    sendAuditInformation(OperationPerformed.UPDATE, authUser, request.getUser(), new RequestDto(oldRequest),
        requestDto);
    request = requestService.handleRequestStatusUpdate(request, authUser);

    return new ResponseEntity<>(new RequestDto(request), HttpStatus.OK);
  }

  private void sendAuditInformation(OperationPerformed operationPerformed, User authUser, User modifiedUser,
      RequestDto previousState, RequestDto currentState) {

    auditInformationService.buildAuditInformation(operationPerformed, AuditedEntity.USER, authUser, modifiedUser,
        previousState, currentState);
  }
}