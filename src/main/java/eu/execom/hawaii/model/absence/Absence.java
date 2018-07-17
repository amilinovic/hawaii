package eu.execom.hawaii.model.absence;

import eu.execom.hawaii.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="absence_type")
public abstract class Absence extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -3525779850487542671L;

  @Column(name = "absence_type", insertable = false, updatable = false)
  @Enumerated(EnumType.STRING)
  private AbsenceType absenceType;

  @NotNull
  private String name;

  private String comment;

  private boolean active;

}
