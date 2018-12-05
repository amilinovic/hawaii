package eu.execom.hawaii.model;

import eu.execom.hawaii.model.enumerations.UserRole;
import eu.execom.hawaii.model.enumerations.UserStatusType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@ToString(exclude = {"approverTeams", "requests", "allowances", "userPushTokens"})
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

  @NotNull
  @Enumerated(EnumType.STRING)
  private UserStatusType userStatusType;

  private int yearsOfService;

  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
  private List<UserPushToken> userPushTokens;

  @ManyToMany(mappedBy = "teamApprovers", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<Team> approverTeams;

  @OneToMany(mappedBy = "user")
  private List<Request> requests;

  @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
  private List<Allowance> allowances;

}