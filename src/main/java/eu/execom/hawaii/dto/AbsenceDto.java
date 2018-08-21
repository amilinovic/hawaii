package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.Absence;
import eu.execom.hawaii.model.enumerations.AbsenceSubtype;
import eu.execom.hawaii.model.enumerations.AbsenceType;
import lombok.Data;

@Data
public class AbsenceDto {

  private Long id;
  private AbsenceType absenceType;
  private AbsenceSubtype absenceSubtype;
  private String name;
  private String comment;
  private boolean active;
  private String iconUrl;

  public AbsenceDto(Absence absence) {
    this.id = absence.getId();
    this.absenceSubtype = absence.getAbsenceSubtype();
    this.name = absence.getName();
    this.comment = absence.getComment();
    this.active = absence.isActive();
    this.iconUrl = absence.getIconUrl();
    this.absenceType = absence.getAbsenceType();
  }

}
