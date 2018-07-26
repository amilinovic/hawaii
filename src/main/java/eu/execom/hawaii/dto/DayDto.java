package eu.execom.hawaii.dto;

import java.time.LocalDate;

import eu.execom.hawaii.model.Day;
import eu.execom.hawaii.model.enumerations.Duration;
import lombok.Data;

@Data
public class DayDto {

  private Long id;
  private Long requestId;
  private LocalDate date;
  private Duration duration;

  public DayDto(Day day) {
    this.id = day.getId();
    this.requestId = day.getRequest().getId();
    this.date = day.getDate();
    this.duration = day.getDuration();
  }

}
