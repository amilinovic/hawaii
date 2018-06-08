package eu.execom.hawaii.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import eu.execom.hawaii.model.enumerations.AbsenceType;
import lombok.Data;

@Entity
@Data
public class Absence extends BaseEntity {

  @Column
  private String name;

  @Column
  private AbsenceType absenceType;

  @Column
  private String comment;

  @Column
  private boolean deducted;

  @OneToMany(mappedBy = "absence")
  private List<Request> requests;
}
