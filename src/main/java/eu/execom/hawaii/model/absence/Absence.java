package eu.execom.hawaii.model.absence;

import java.io.Serializable;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;

import eu.execom.hawaii.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="absence_type")
public abstract class Absence extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -3525779850487542671L;

  @NotNull
  private String name;

  private String comment;

}
