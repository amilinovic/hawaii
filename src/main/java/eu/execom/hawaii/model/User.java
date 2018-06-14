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
  private Team team;

  @ManyToOne
  private LeaveProfile leaveProfile;

  @NotNull
  private String fullName;

  @NotNull
  private String email;

  @NotNull
  @Enumerated(EnumType.STRING)
  private UserRole userRole;

  private String jobTitle;

  private boolean active;

  @OneToMany(mappedBy = "user")
  private List<TeamApprover> teamApprovers;

  @OneToMany(mappedBy = "user")
  private List<Request> requests;

  @OneToMany(mappedBy = "user")
  private List<Allowance> allowances;

}
