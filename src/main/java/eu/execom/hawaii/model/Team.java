package eu.execom.hawaii.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@ToString(exclude = {"users", "teamApprovers"})
@EqualsAndHashCode(callSuper = false)
public class Team extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -448979442951482200L;

  @NotNull
  private String name;

  private String emails;

  private boolean active;

  @OneToMany(mappedBy = "team", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<User> users;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(name = "team_approver", joinColumns = @JoinColumn(name = "team_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
  private List<User> teamApprovers;

}
