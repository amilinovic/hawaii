package eu.execom.hawaii.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Year extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 7347843784040392307L;

  @Column(unique = true)
  private int year;

  /*@OneToMany(mappedBy = "allowance", fetch = FetchType.EAGER)
  private List<Allowance> allowances;*/
}
