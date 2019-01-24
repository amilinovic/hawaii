package eu.execom.hawaii.model;

import eu.execom.hawaii.model.enumerations.LeaveProfileType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@ToString(exclude = "users")
@EqualsAndHashCode(callSuper = false)
public class LeaveProfile extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -7099701790737670685L;

  @NotNull
  private String name;

  private int entitlement;

  private int maxCarriedOver;

  private int maxBonusDays;

  private int training;

  private boolean deleted;

  private boolean upgradeable;

  @NotNull
  @Enumerated(EnumType.STRING)
  private LeaveProfileType leaveProfileType;

  private String comment;

  @OneToMany(mappedBy = "leaveProfile", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<User> users;

}
