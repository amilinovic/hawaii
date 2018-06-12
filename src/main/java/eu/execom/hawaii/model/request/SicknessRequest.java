package eu.execom.hawaii.model.request;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.enumerations.RequestStatus;

@Entity
public class SicknessRequest extends Request {

  @ManyToOne
  private SicknessType sicknessType;

  @Override
  public void approve() {
    this.getUser().addSickDays(this.getDays().size());
    this.setRequestStatus(RequestStatus.APPROVED);
  }
}
