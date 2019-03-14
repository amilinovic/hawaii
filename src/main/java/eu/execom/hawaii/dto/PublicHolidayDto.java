package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.PublicHoliday;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class PublicHolidayDto {

  private Long id;

  @NotNull
  private String name;

  @NotNull
  private LocalDate date;

  @NotNull
  private boolean deleted;

  public PublicHolidayDto(PublicHoliday publicHoliday) {
    this.id = publicHoliday.getId();
    this.deleted = publicHoliday.isDeleted();
    this.name = publicHoliday.getName();
    this.date = publicHoliday.getDate();
  }

}
