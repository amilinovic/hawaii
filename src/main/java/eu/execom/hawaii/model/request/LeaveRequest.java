package eu.execom.hawaii.model.request;

import javax.persistence.Column;
import javax.persistence.Entity;

import eu.execom.hawaii.model.Request;

@Entity
public class LeaveRequest extends Request {

  @Column
  private LeaveType leaveType;

  @Override
  public void approve() {
    this.getUser().addTakenDays(this.getDays().size());
  }
}
