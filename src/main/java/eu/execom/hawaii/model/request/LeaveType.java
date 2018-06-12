package eu.execom.hawaii.model.request;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import eu.execom.hawaii.model.AbsenceType;
import eu.execom.hawaii.model.Request;

@Entity
public class LeaveType extends AbsenceType {

  private boolean deducted;

  @OneToMany(mappedBy = "leaveType", targetEntity = LeaveRequest.class)
  private List<Request> requests;


}
