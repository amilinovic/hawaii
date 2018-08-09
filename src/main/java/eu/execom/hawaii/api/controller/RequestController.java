package eu.execom.hawaii.api.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import eu.execom.hawaii.model.enumerations.AbsenceType;
import eu.execom.hawaii.model.enumerations.RequestStatus;
import eu.execom.hawaii.service.RequestService;

@RestController
@RequestMapping("/requests")
public class RequestController {

  private static final ModelMapper MAPPER = new ModelMapper();

  private RequestService requestService;

  @Autowired
  public RequestController(RequestService requestService) {
    this.requestService = requestService;
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
    request = requestService.save(request);

    return new ResponseEntity<>(new RequestDto(request), HttpStatus.OK);
  }

  @PutMapping
  public ResponseEntity<RequestDto> handleRequestStatus(@RequestBody RequestDto requestDto) {
    var request = MAPPER.map(requestDto, Request.class);
    request = requestService.handleRequestStatusUpdate(request);

    return new ResponseEntity<>(new RequestDto(request), HttpStatus.OK);
  }

}