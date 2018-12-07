package eu.execom.hawaii.api.controller;

import eu.execom.hawaii.repository.YearRepository;
import eu.execom.hawaii.service.YearService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/years")
public class YearController {

  private static final ModelMapper MAPPER = new ModelMapper();

  private YearRepository yearRepository;
  private YearService yearService;

  @Autowired
  public YearController(YearService yearService, YearRepository yearRepository) {
    this.yearService = yearService;
    this.yearRepository = yearRepository;
  }

 /* @PostMapping
  public ResponseEntity<YearDto> createYear(@RequestBody YearDto yearDto) {
    Year year = yearRepository.findOneByYear(yearDto.getYear());
    if(year != null ) {
      Year year = MAPPER.map(yearDto, Year.class);
      yearService.saveYear(year);
    } else {
      log.error("Year already exists, cannot create same year twice!");
    }
    return new ResponseEntity<>(new YearDto(year), HttpStatus.CREATED);

  }*/
}
