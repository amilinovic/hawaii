package eu.execom.hawaii.model.request;

import javax.persistence.Column;
import javax.persistence.Entity;

import eu.execom.hawaii.model.Absence;

@Entity
public class LeaveType extends Absence {

  @Column
  private boolean deducted;

}
