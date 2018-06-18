package eu.execom.hawaii.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class TeamApprover extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 2606476682255482836L;

  @ManyToOne
  private Team team;

  @ManyToOne
  private User user;

}
