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
public class Leave extends Absence {

  private static final long serialVersionUID = -6517365230450643062L;

  private boolean deducted;

  @OneToMany(mappedBy = "absence")
  private List<Request> leaveRequests;

}
