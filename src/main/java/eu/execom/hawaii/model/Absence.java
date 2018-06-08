package eu.execom.hawaii.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import eu.execom.hawaii.model.enumerations.AbsenceType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Absence extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -3525779850487542671L;

  @Column
  @NotNull
  private String name;

  @Column
  @Enumerated(EnumType.STRING)
  @NotNull
  private AbsenceType absenceType;

  @Column
  private String comment;

  @Column
  private boolean deducted;

  @OneToMany(mappedBy = "absence")
  private List<Request> requests;
}
