package eu.execom.hawaii.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
public class LeaveProfile extends BaseEntity {

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
