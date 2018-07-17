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
@DiscriminatorValue("BONUS_DAYS")
public class BonusDays extends Absence {

  private static final long serialVersionUID = -2588261514154877517L;

  @OneToMany(mappedBy = "absence")
  private List<Request> bonusDaysRequests;

}
