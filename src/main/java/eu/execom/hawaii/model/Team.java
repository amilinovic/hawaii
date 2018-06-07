package eu.execom.hawaii.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
public class Team implements Serializable {

  private static final long serialVersionUID = -448979442951482200L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String name;

  private String emails;

  @OneToMany(mappedBy = "team")
  private Set<ManagerTeam> teamManagers;

  @OneToMany(mappedBy = "team")
  private Set<User> users;
}
