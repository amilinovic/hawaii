package eu.execom.hawaii.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class LeaveType {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  private String comment;

  private boolean deducted;
}
