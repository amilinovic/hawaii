package eu.execom.hawaii.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import eu.execom.hawaii.model.enumerations.AbsenceType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Absence extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -7375197378676775460L;

  @NotNull
  @Enumerated(EnumType.STRING)
  private AbsenceType absenceType;

  @NotNull
  private String name;

  private String comment;

  private boolean active;

  private boolean deducted;

  @OneToMany(mappedBy = "absence")
  private List<Request> leaveRequests;

}