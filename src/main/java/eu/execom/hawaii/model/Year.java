package eu.execom.hawaii.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Year extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 7347843784040392307L;

  private boolean active;

  @Column(unique = true)
  private int year;

  @OneToMany(mappedBy = "year", fetch = FetchType.EAGER)
  private List<Allowance> allowances;
}
