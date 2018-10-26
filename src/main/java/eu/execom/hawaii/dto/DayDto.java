package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.Day;
import eu.execom.hawaii.model.enumerations.Duration;
import lombok.Data;

import java.time.LocalDate;

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
