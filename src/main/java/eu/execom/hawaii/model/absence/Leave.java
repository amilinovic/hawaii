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
@DiscriminatorValue("LEAVE")
public class Leave extends Absence {

  private static final long serialVersionUID = -6517365230450643062L;

  private boolean deducted;

  @OneToMany(mappedBy = "absence")
  private List<Request> leaveRequests;

}
