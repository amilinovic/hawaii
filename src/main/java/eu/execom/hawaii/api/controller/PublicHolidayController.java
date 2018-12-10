package eu.execom.hawaii.api.controller;

import eu.execom.hawaii.dto.PublicHolidayDto;
import eu.execom.hawaii.model.PublicHoliday;
import eu.execom.hawaii.service.PublicHolidayService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/publicholidays")
public class PublicHolidayController {

  private static final ModelMapper MAPPER = new ModelMapper();

  private PublicHolidayService publicHolidayService;

  @Autowired
  public PublicHolidayController(PublicHolidayService publicHolidayService) {
    this.publicHolidayService = publicHolidayService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<PublicHolidayDto> getPublicHoliday(@PathVariable Long id) {
    var publicHoliday = publicHolidayService.getById(id);
    var publicHolidayDto = new PublicHolidayDto(publicHoliday);

    return new ResponseEntity<>(publicHolidayDto, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<PublicHolidayDto>> getPublicHolidays(@RequestParam boolean deleted) {
    var publicHolidays = publicHolidayService.findAllByDeleted(deleted);
    var publicHolidayDtos = publicHolidays.stream().map(PublicHolidayDto::new).collect(Collectors.toList());

    return new ResponseEntity<>(publicHolidayDtos, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<PublicHolidayDto> createPublicHoliday(@RequestBody PublicHolidayDto publicHolidayDto) {
    var publicHoliday = MAPPER.map(publicHolidayDto, PublicHoliday.class);
    publicHoliday = publicHolidayService.save(publicHoliday);
    var publicHolidayDtoResponse = new PublicHolidayDto(publicHoliday);

    return new ResponseEntity<>(publicHolidayDtoResponse, HttpStatus.OK);
  }

  @PutMapping
  public ResponseEntity<PublicHolidayDto> updatePublicHoliday(@RequestBody PublicHolidayDto publicHolidayDto) {
    var publicHoliday = MAPPER.map(publicHolidayDto, PublicHoliday.class);
    var publicHolidayDtoResponse = new PublicHolidayDto(publicHoliday);

    return new ResponseEntity<>(publicHolidayDtoResponse, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity deletePublicHoliday(@PathVariable Long id) {
    publicHolidayService.delete(id);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }



}
