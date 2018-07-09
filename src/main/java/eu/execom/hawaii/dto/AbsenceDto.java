package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.absence.Absence;
import lombok.Data;

@Data
public class AbsenceDto {

  private Long id;
  private String absenceType;
  private String name;
  private String comment;
  private boolean active;
  private boolean deducted;

  public AbsenceDto(Absence absence) {
    this.id = absence.getId();
    this.name = absence.getName();
    this.comment = absence.getComment();
    this.active = absence.isActive();
  }
}
