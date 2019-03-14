package eu.execom.hawaii.api.controller;

import eu.execom.hawaii.dto.YearDto;
import eu.execom.hawaii.exceptions.ActionNotAllowedException;
import eu.execom.hawaii.model.Year;
import eu.execom.hawaii.service.YearService;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequestMapping("/api/years")
public class YearController {

  private static final ModelMapper MAPPER = new ModelMapper();

  private YearService yearService;

  @Autowired
  public YearController(YearService yearService) {
    this.yearService = yearService;
  }

  @PostMapping
  public ResponseEntity<YearDto> createYear(@RequestBody YearDto yearDto) {
    Year year;
    if (!yearService.existsByYear(yearDto.getYear())) {
      year = MAPPER.map(yearDto, Year.class);
      yearService.save(year);
      yearService.createAllowanceOnCreateYear(year);
    } else {
      throw new ActionNotAllowedException("This year already exists.");
    }
    return new ResponseEntity<>(new YearDto(year), HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<YearDto> getYear(@PathVariable Long id) {
    var year = yearService.getById(id);
    var yearDto = new YearDto(year);

    return new ResponseEntity<>(yearDto, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<YearDto>> getAllYears() {
    var allYears = yearService.getAll();
    var allYearsDto = allYears.stream().map(YearDto::new).collect(Collectors.toList());

    return new ResponseEntity<>(allYearsDto, HttpStatus.OK);
  }

  @PutMapping
  public ResponseEntity<YearDto> updateYear(@RequestBody YearDto yearDto) {
    var year = MAPPER.map(yearDto, Year.class);

    if (year.getAllowances().isEmpty()) {
      yearService.createAllowanceOnCreateYear(year);
    }
    year = yearService.save(year);

    return new ResponseEntity<>(new YearDto(year), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity deleteYear(@PathVariable Long id) {
    yearService.delete(id);

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }
}
