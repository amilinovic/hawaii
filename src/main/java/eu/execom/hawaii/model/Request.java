package eu.execom.hawaii.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import eu.execom.hawaii.model.absence.Absence;
import eu.execom.hawaii.model.enumerations.RequestStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Request extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -2334871552852759172L;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @NotNull
  @Enumerated(EnumType.STRING)
  private RequestStatus requestStatus;

  private String reason;

  @OneToMany(mappedBy = "request")
  private List<Day> days;

  @ManyToOne
  private Absence absence;

  @ManyToOne
  private User approver;

}
