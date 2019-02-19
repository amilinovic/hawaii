package eu.execom.hawaii.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@ToString(exclude = {"users", "teamApprovers"})
@EqualsAndHashCode(callSuper = false)
public class Team extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -448979442951482200L;

  @NotNull
  @Column(unique = true)
  private String name;

  private String sicknessRequestEmails;

  private String annualRequestEmails;

  private String bonusRequestEmails;

  private boolean sendEmailToTeammatesForSicknessRequestEnabled;

  private boolean sendEmailToTeammatesForAnnualRequestEnabled;

  private boolean sendEmailToTeammatesForBonusRequestEnabled;

  private boolean deleted;

  @OneToMany(mappedBy = "team", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<User> users;

  @ManyToMany(cascade = {CascadeType.MERGE})
  @JoinTable(name = "team_approver", joinColumns = @JoinColumn(name = "team_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
  private List<User> teamApprovers;

}
