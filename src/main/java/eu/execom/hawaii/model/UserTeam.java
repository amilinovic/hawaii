package eu.execom.hawaii.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class UserTeam implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private Long id;

  @ManyToOne
  private User user;

  @ManyToOne
  private Team team;
}
