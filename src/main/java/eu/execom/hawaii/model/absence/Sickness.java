package eu.execom.hawaii.model.absence;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import eu.execom.hawaii.model.request.Request;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Sickness extends Absence {

  private static final long serialVersionUID = -1173499998168099381L;

  @OneToMany(mappedBy = "absence")
  private List<Request> sicknessRequests;

}
