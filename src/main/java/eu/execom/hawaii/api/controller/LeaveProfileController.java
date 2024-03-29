package eu.execom.hawaii.api.controller;

import eu.execom.hawaii.dto.LeaveProfileDto;
import eu.execom.hawaii.model.LeaveProfile;
import eu.execom.hawaii.repository.LeaveProfileRepository;
import eu.execom.hawaii.service.LeaveProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/leaveprofiles")
public class LeaveProfileController {

  private static final ModelMapper MAPPER = new ModelMapper();

  private LeaveProfileService leaveProfileService;
  private LeaveProfileRepository leaveProfileRepository;

  @Autowired
  public LeaveProfileController(LeaveProfileService leaveProfileService,
      LeaveProfileRepository leaveProfileRepository) {
    this.leaveProfileService = leaveProfileService;
    this.leaveProfileRepository = leaveProfileRepository;
  }

  @GetMapping
  public ResponseEntity<List<LeaveProfileDto>> getLeaveProfiles() {
    var leaveProfiles = leaveProfileService.findAll();
    var leaveProfileDtos = leaveProfiles.stream().map(LeaveProfileDto::new).collect(Collectors.toList());
    return new ResponseEntity<>(leaveProfileDtos, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<LeaveProfileDto> getLeaveProfile(@PathVariable Long id) {
    var leaveProfile = leaveProfileService.getById(id);
    var leaveProfileDto = new LeaveProfileDto(leaveProfile);
    return new ResponseEntity<>(leaveProfileDto, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<LeaveProfileDto> createLeaveProfile(@RequestBody LeaveProfileDto leaveProfileDto) {
    var leaveProfile = MAPPER.map(leaveProfileDto, LeaveProfile.class);
    leaveProfile = leaveProfileService.create(leaveProfile);
    var leaveProfileDtoResponse = new LeaveProfileDto(leaveProfile);
    return new ResponseEntity<>(leaveProfileDtoResponse, HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<LeaveProfileDto> updateLeaveProfile(@RequestBody LeaveProfileDto leaveProfileDto) {
    LeaveProfile leaveProfile = MAPPER.map(leaveProfileDto, LeaveProfile.class);
    leaveProfile = leaveProfileRepository.save(leaveProfile);
    return new ResponseEntity<>(new LeaveProfileDto(leaveProfile), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity deleteLeaveProfile(@PathVariable Long id) {
    leaveProfileService.delete(id);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }
}
