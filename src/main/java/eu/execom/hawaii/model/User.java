package eu.execom.hawaii.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Where;

import eu.execom.hawaii.model.enumerations.UserRole;
import lombok.Data;

@Entity
@Data
@Where(clause = "active = 'false'")
public class User implements Serializable {

  private static final long serialVersionUID = 950754391551134726L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
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

  @OneToMany
  private List<ManagerTeam> managerTeams;

  @OneToMany(mappedBy = "user")
  private List<Request> requests;

  @OneToMany(mappedBy = "user")
  private List<Allowance> allowance;

  private LeaveProfile leaveProfile;

}
