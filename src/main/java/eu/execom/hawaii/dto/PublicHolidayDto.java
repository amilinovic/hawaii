package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.PublicHoliday;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PublicHolidayDto {

  private boolean active;
  private String name;
  private LocalDate date;

  public PublicHolidayDto(PublicHoliday publicHoliday) {
    this.active = publicHoliday.isActive();
    this.name = publicHoliday.getName();
    this.date = publicHoliday.getDate();
  }

}
