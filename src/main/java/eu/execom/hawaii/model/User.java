package eu.execom.hawaii.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import eu.execom.hawaii.model.enumerations.UserRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@ToString(exclude = {"approverTeams", "requests", "allowances"})
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
  @Email
  @Column(unique = true)
  private String email;

  @NotNull
  @Enumerated(EnumType.STRING)
  private UserRole userRole;

  private String jobTitle;

  private boolean active;

  private int yearsOfService;

  private String pushToken;

  @ManyToMany(mappedBy = "teamApprovers", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<Team> approverTeams;

  @OneToMany(mappedBy = "user")
  private List<Request> requests;

  @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
  private List<Allowance> allowances;

}
