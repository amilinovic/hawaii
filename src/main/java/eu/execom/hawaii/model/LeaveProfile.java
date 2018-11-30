package eu.execom.hawaii.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
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

  private String comment;

  @OneToMany(mappedBy = "leaveProfile", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<User> users;

}
