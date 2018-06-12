package eu.execom.hawaii.model.request;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.enumerations.RequestStatus;

@Entity
public class LeaveRequest extends Request {

  @ManyToOne
  private LeaveType leaveType;

  @Override
  public void approve() {
    this.getUser().addTakenDays(this.getDays().size());
    this.setRequestStatus(RequestStatus.APPROVED);
  }
}
