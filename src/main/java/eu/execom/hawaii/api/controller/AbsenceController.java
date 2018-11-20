package eu.execom.hawaii.api.controller;

import eu.execom.hawaii.dto.AbsenceDto;
import eu.execom.hawaii.model.Absence;
import eu.execom.hawaii.service.AbsenceService;
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
@RequestMapping("/api/leavetypes")
public class AbsenceController {

  private static final ModelMapper MAPPER = new ModelMapper();

  private AbsenceService absenceService;

  @Autowired
  public AbsenceController(AbsenceService absenceService) {
    this.absenceService = absenceService;
  }

  @GetMapping
  public ResponseEntity<List<AbsenceDto>> getAbsences() {
    var absences = absenceService.findAll();
    var absenceDtos = absences.stream().map(AbsenceDto::new).collect(Collectors.toList());
    return new ResponseEntity<>(absenceDtos, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<AbsenceDto> getAbsence(@PathVariable Long id) {
    var absence = absenceService.getById(id);
    var absenceDto = new AbsenceDto(absence);
    return new ResponseEntity<>(absenceDto, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<AbsenceDto> createAbsence(@RequestBody AbsenceDto absenceDto) {
    absenceDto = saveAbsenceDto(absenceDto);
    return new ResponseEntity<>(absenceDto, HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<AbsenceDto> updateAbsence(@RequestBody AbsenceDto absenceDto) {
    absenceDto = saveAbsenceDto(absenceDto);
    return new ResponseEntity<>(absenceDto, HttpStatus.OK);
  }

  private AbsenceDto saveAbsenceDto(@RequestBody AbsenceDto absenceDto) {
    var absence = MAPPER.map(absenceDto, Absence.class);
    absence = absenceService.save(absence);
    absenceDto = new AbsenceDto(absence);
    return absenceDto;
  }

  @DeleteMapping("/{id}")
  public ResponseEntity deleteAbsence(@PathVariable Long id) {
    absenceService.delete(id);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }
}
