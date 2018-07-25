package eu.execom.hawaii.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.execom.hawaii.dto.DayDto;
import eu.execom.hawaii.dto.RequestDto;
import eu.execom.hawaii.model.Day;
import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.enumerations.AbsenceType;
import eu.execom.hawaii.model.enumerations.RequestStatus;
import eu.execom.hawaii.repository.DayRepository;
import eu.execom.hawaii.service.AbsenceService;
import eu.execom.hawaii.service.RequestService;

@RestController
@RequestMapping("/requests")
public class RequestController {

  private static final ModelMapper MAPPER = new ModelMapper();

  private RequestService requestService;
  private AbsenceService absenceService;
  private DayRepository dayRepository;

  @Autowired
  public RequestController(RequestService requestService, AbsenceService absenceService, DayRepository dayRepository) {
    this.requestService = requestService;
    this.absenceService = absenceService;
    this.dayRepository = dayRepository;
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<List<RequestDto>> getRequestsByUserId(@PathVariable Long id) {
    var requests = requestService.findAllByUser(id);
    var requestDtos = requests.stream().map(RequestDto::new).collect(Collectors.toList());

    return new ResponseEntity<>(requestDtos, HttpStatus.OK);
  }

  @GetMapping("/approver/{id}")
  public ResponseEntity<List<RequestDto>> getRequestsByApproverId(@PathVariable Long id) {
    var requests = requestService.findAllByApprover(id);
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
    // Map and save request.
    var request = mapAndSaveRequest(requestDto);
    var requestId = request.getId();

    // Map and save request days.
    requestDto.getDayDtos().forEach(dayDto -> dayDto.setRequestId(requestId));
    var days = mapDays(requestDto.getDayDtos());
    days = dayRepository.saveAll(days);
    request.setDays(days);

    return new ResponseEntity<>(new RequestDto(request), HttpStatus.OK);
  }

  @PutMapping
  public ResponseEntity<RequestDto> handleRequestStatus(@RequestBody RequestDto requestDto) {
    // Request
    var request = MAPPER.map(requestDto, Request.class);
    // Request days
    var days = mapDays(requestDto.getDayDtos());
    request.setDays(days);
    // Request absence
    request.setAbsence(absenceService.getById(requestDto.getAbsenceId()));
    request = requestService.handleRequestStatusUpdate(request);

    return new ResponseEntity<>(new RequestDto(request), HttpStatus.OK);
  }

  private Request mapAndSaveRequest(RequestDto requestDto) {
    var request = MAPPER.map(requestDto, Request.class);
    return requestService.save(request);
  }

  private List<Day> mapDays(List<DayDto> dayDtos) {
    return dayDtos.stream().map(dayDto -> MAPPER.map(dayDto, Day.class)).collect(Collectors.toList());
  }

}