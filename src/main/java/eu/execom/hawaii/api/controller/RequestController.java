package eu.execom.hawaii.api.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.execom.hawaii.dto.RequestDto;
import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.Team;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.AbsenceType;
import eu.execom.hawaii.model.enumerations.RequestStatus;
import eu.execom.hawaii.service.RequestService;
import eu.execom.hawaii.service.UserService;

@RestController
@RequestMapping("/requests")
public class RequestController {

  private static final ModelMapper MAPPER = new ModelMapper();

  private RequestService requestService;
  private UserService userService;

  @Autowired
  public RequestController(RequestService requestService, UserService userService) {
    this.requestService = requestService;
    this.userService = userService;
  }

  @GetMapping("/approval")
  public ResponseEntity<List<RequestDto>> getAllRequestsForApproval(Principal principal) {
    User authUser = getUserFromPrincipal(principal);
    List<Request> requests = getRequestsForApprover(authUser.getApproverTeams());
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
        || RequestStatus.CANCELATION_PENDING.equals(request.getRequestStatus());
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<List<RequestDto>> getRequestsByUserId(@PathVariable Long id) {
    var requests = requestService.findAllByUser(id);
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
  public ResponseEntity<RequestDto> createRequest(@RequestBody RequestDto requestDto) {
    var request = MAPPER.map(requestDto, Request.class);
    request = requestService.create(request);

    return new ResponseEntity<>(new RequestDto(request), HttpStatus.OK);
  }

  @PutMapping
  public ResponseEntity<RequestDto> handleRequestStatus(Principal principal, @RequestBody RequestDto requestDto) {
    var approver = getUserFromPrincipal(principal);

    var request = MAPPER.map(requestDto, Request.class);
    request = requestService.handleRequestStatusUpdate(request, approver);

    return new ResponseEntity<>(new RequestDto(request), HttpStatus.OK);
  }

  private User getUserFromPrincipal(Principal principal) {
    OAuth2Authentication auth = (OAuth2Authentication) principal;
    UsernamePasswordAuthenticationToken authUser = (UsernamePasswordAuthenticationToken) auth.getUserAuthentication();
    LinkedHashMap userDetails = (LinkedHashMap) authUser.getDetails();
    String userEmail = (String) userDetails.get("email");

    return userService.findByEmail(userEmail);
  }

}