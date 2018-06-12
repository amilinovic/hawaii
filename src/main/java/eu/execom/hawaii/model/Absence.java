package eu.execom.hawaii.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Absence extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -3525779850487542671L;

  @Column
  @NotNull
  private String name;

  @Column
  private String comment;

//  @OneToMany(mappedBy = "absence")
//  private List<Request> requests;
}
