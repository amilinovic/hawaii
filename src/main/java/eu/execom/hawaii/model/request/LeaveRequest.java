package eu.execom.hawaii.model.request;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import eu.execom.hawaii.model.absence.Leave;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class LeaveRequest extends Request {

  private static final long serialVersionUID = -8246653641796359126L;

  @ManyToOne
  private Leave leave;

}
