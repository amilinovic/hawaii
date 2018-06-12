package eu.execom.hawaii.model.request;

import javax.persistence.Entity;

import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.enumerations.RequestStatus;

@Entity
public class BonusRequest extends Request {

  @Override
  public void approve() {
    this.getUser().addBonusDays(this.getDays().size());
    this.setRequestStatus(RequestStatus.APPROVED);
  }
}
