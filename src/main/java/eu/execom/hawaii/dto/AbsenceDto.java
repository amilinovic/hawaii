package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.Absence;
import eu.execom.hawaii.model.enumerations.AbsenceSubtype;
import eu.execom.hawaii.model.enumerations.AbsenceType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AbsenceDto {

  private Long id;

  @NotNull
  private AbsenceType absenceType;

  private AbsenceSubtype absenceSubtype;

  @NotNull
  private String name;

  private String comment;

  @NotNull
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
