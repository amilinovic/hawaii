package eu.execom.hawaii.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class LeaveProfile extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -7099701790737670685L;

  private String name;

  private String comment;

  private int entitlement;

  private int maxCarriedOver;

  private int training;

  @OneToMany(mappedBy = "leaveProfile")
  private List<PublicHoliday> publicHolidays;

  @OneToMany(mappedBy = "leaveProfile")
  private List<User> users;

}
