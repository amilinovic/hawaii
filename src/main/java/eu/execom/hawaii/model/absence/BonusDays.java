package eu.execom.hawaii.model.absence;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import eu.execom.hawaii.model.Request;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("bonus")
public class BonusDays extends Absence {

  private static final long serialVersionUID = -2588261514154877517L;

  @OneToMany(mappedBy = "absence")
  private List<Request> bonusDaysRequests;
}
