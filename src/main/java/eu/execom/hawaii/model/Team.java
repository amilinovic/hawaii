package eu.execom.hawaii.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
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
  private String name;

  private String emails;

  private boolean active;

  @OneToMany(mappedBy = "team")
  private List<User> users;

  @OneToMany(mappedBy = "team")
  private List<TeamApprover> teamApprovers;

}
