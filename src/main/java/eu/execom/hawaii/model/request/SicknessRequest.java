package eu.execom.hawaii.model.request;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import eu.execom.hawaii.model.absence.Sickness;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class SicknessRequest extends Request {

  private static final long serialVersionUID = -1814915934204264243L;

  @ManyToOne
  private Sickness sickness;

}
