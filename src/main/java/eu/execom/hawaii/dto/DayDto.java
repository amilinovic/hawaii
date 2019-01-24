package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.Day;
import eu.execom.hawaii.model.enumerations.AbsenceSubtype;
import eu.execom.hawaii.model.enumerations.AbsenceType;
import eu.execom.hawaii.model.enumerations.Duration;
import eu.execom.hawaii.model.enumerations.RequestStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DayDto {

  private Long id;
  private Long requestId;
  private LocalDate date;
  private Duration duration;
  private RequestStatus requestStatus;
  private String iconUrl;
  private AbsenceType absenceType;
  private AbsenceSubtype absenceSubtype;


  public DayDto(Day day) {
    this.id = day.getId();
    this.requestId = day.getRequest().getId();
    this.date = day.getDate();
    this.duration = day.getDuration();
    this.requestStatus = day.getRequest().getRequestStatus();
    this.absenceType = day.getRequest().getAbsence().getAbsenceType();
    this.absenceSubtype = day.getRequest().getAbsence().getAbsenceSubtype();
    this.iconUrl = day.getRequest().getAbsence().getIconUrl();
  }

}
