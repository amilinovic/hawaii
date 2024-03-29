package eu.execom.hawaii.model;

import eu.execom.hawaii.model.enumerations.AbsenceSubtype;
import eu.execom.hawaii.model.enumerations.AbsenceType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Absence extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -7375197378676775460L;

  @NotNull
  @Enumerated(EnumType.STRING)
  private AbsenceType absenceType;

  @Enumerated(EnumType.STRING)
  private AbsenceSubtype absenceSubtype;

  @NotNull
  @Column(unique = true)
  private String name;

  private String comment;

  @NotNull
  private boolean active;

  private String iconUrl;

  @OneToMany(mappedBy = "absence")
  private List<Request> leaveRequests;

  public boolean isBonusDays() {
    return AbsenceType.BONUS_DAYS.equals(absenceType);
  }

}