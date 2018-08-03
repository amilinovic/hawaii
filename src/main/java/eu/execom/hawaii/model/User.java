package eu.execom.hawaii.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import eu.execom.hawaii.model.enumerations.UserRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@ToString(exclude = {"teamApprovers", "requests", "allowances"})
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

  @ManyToMany(mappedBy = "teamApprovers", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<Team> teamApprovers = new ArrayList<>();

  @OneToMany(mappedBy = "user")
  private List<Request> requests;

  @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<Allowance> allowances;

}
