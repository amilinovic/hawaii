package eu.execom.hawaii.model.request;

import javax.persistence.Entity;

import eu.execom.hawaii.model.Request;

@Entity
public class BonusRequest extends Request {

  @Override
  public void approve() {
    this.getUser().addBonusDays(this.getDays().size());
  }
}
