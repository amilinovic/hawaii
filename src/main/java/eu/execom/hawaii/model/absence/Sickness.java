package eu.execom.hawaii.model.absence;

import eu.execom.hawaii.model.Request;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("SICKNESS")
public class Sickness extends Absence {

  private static final long serialVersionUID = -1173499998168099381L;

  @OneToMany(mappedBy = "absence")
  private List<Request> sicknessRequests;

}
