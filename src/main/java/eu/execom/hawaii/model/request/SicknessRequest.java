package eu.execom.hawaii.model.request;

import javax.persistence.Entity;

import eu.execom.hawaii.model.Request;

@Entity
public class SicknessRequest extends Request {

  private SicknessType sicknessType;

  @Override
  public void approve() {
    this.getUser().addSickDays(this.getDays().size());
  }
}
