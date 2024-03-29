package eu.execom.hawaii.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Year extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 7347843784040392307L;

  @NotNull
  private boolean active;

  @NotNull
  @Column(unique = true)
  private int year;

  @OneToMany(mappedBy = "year", fetch = FetchType.EAGER)
  private List<Allowance> allowances;
}
