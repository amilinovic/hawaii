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

  @ManyToOne
  private LeaveProfile leaveProfile;

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

}
