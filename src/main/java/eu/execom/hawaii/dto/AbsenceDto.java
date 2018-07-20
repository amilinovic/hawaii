package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.Absence;
import eu.execom.hawaii.model.enumerations.AbsenceType;
import lombok.Data;

@Data
public class AbsenceDto {

  private Long id;
  private AbsenceType absenceType;
  private String name;
  private String comment;
  private boolean active;
  private boolean deducted;

  public AbsenceDto(Absence absence) {
    this.id = absence.getId();
    this.name = absence.getName();
    this.comment = absence.getComment();
    this.active = absence.isActive();
    this.absenceType = absence.getAbsenceType();
    this.deducted = absence.isDeducted();
  }

}
