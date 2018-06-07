package eu.execom.hawaii.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class LeaveProfileDetail {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  private int entitlement;

  private int maxCarriedOver;
}
