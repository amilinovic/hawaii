package eu.execom.hawaii.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Team extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -448979442951482200L;

  @NotNull
  private String name;

  private String emails;

  @OneToMany(mappedBy = "team")
  private List<User> users;

  @OneToMany(mappedBy = "team")
  private List<TeamApprover> teamApprovers;
}
