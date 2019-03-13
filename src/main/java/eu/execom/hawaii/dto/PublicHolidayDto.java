package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.PublicHoliday;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PublicHolidayDto {

  private Long id;
  private boolean deleted;
  private String name;
  private LocalDate date;

  public PublicHolidayDto(PublicHoliday publicHoliday) {
    this.id = publicHoliday.getId();
    this.deleted = publicHoliday.isDeleted();
    this.name = publicHoliday.getName();
    this.date = publicHoliday.getDate();
  }

}
