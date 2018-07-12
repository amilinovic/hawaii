package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.absence.Absence;
import eu.execom.hawaii.model.absence.AbsenceType;
import eu.execom.hawaii.model.absence.BonusDays;
import eu.execom.hawaii.model.absence.Leave;
import eu.execom.hawaii.model.absence.Sickness;
import lombok.Data;

import static eu.execom.hawaii.model.absence.AbsenceType.BONUS_DAYS;
import static eu.execom.hawaii.model.absence.AbsenceType.LEAVE;
import static eu.execom.hawaii.model.absence.AbsenceType.SICKNESS;

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

    if (absence instanceof Leave) {
      this.absenceType = LEAVE;
      this.deducted = ((Leave) absence).isDeducted();
    } else if (absence instanceof Sickness) {
      this.absenceType = SICKNESS;
    } else if (absence instanceof BonusDays) {
      this.absenceType = BONUS_DAYS;
    }
  }
}
