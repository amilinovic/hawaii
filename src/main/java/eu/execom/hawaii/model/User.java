package eu.execom.hawaii.model;

import eu.execom.hawaii.model.enumerations.UserRole;
import eu.execom.hawaii.model.enumerations.UserStatusType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.sql.Blob;

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

  @NotNull
  private LocalDate startedWorkingDate;

  @NotNull
  private LocalDate startedWorkingAtExecomDate;

  @NotNull
  private int yearsOfService;

  @Lob
  @Basic(fetch = FetchType.LAZY)
  @Column(name = "IMAGE", columnDefinition="MEDIUMBLOB")
  private byte[] image;

  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
  private List<UserPushToken> userPushTokens;

  @ManyToMany(mappedBy = "teamApprovers", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<Team> approverTeams;

  @OneToMany(mappedBy = "user")
  private List<Request> requests;

  @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
  private List<Allowance> allowances;

}