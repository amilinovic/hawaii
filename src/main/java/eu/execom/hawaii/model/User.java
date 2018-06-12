package eu.execom.hawaii.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import eu.execom.hawaii.model.enumerations.UserRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class User extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 950754391551134726L;

  private boolean active;

  @NotNull
  private String fullName;

  private String jobTitle;

  @NotNull
  @Enumerated(EnumType.STRING)
  private UserRole userRole;

  @NotNull
  private String email;

  @ManyToOne
  private Team team;

  @OneToMany(mappedBy = "user")
  private List<TeamApprover> teamApprovers;

  @OneToMany(mappedBy = "user")
  private List<Request> requests;

  @OneToMany(mappedBy = "user")
  private List<Allowance> allowances;

  @ManyToOne
  private LeaveProfile leaveProfile;

  private Allowance getCurrentAllowance() {
    return allowances.get(allowances.size());
  }

  public void addBonusDays(int added) {
    Allowance currentAllowance = getCurrentAllowance();
    int oldBonus = currentAllowance.getBonus();
    currentAllowance.setBonus(oldBonus + added);
  }

  public void addSickDays(int added) {
    Allowance currentAllowance = getCurrentAllowance();
    int oldSickDays = currentAllowance.getSickness();
    currentAllowance.setBonus(oldSickDays + added);
  }

  public void addTakenDays(int taken) {
    Allowance currentAllowance = getCurrentAllowance();
    int oldTaken = currentAllowance.getTaken();
    currentAllowance.setBonus(oldTaken + taken);
  }


}
