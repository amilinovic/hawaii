package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.absence.Absence;
import eu.execom.hawaii.model.absence.AbsenceType;
import eu.execom.hawaii.model.absence.Leave;
import lombok.Data;

import static eu.execom.hawaii.model.absence.AbsenceType.LEAVE;

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

    if (this.absenceType == LEAVE) {
      this.deducted = ((Leave) absence).isDeducted();
    }
  }

}
