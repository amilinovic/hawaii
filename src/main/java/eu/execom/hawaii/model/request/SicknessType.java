package eu.execom.hawaii.model.request;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import eu.execom.hawaii.model.AbsenceType;
import eu.execom.hawaii.model.Request;

@Entity
public class SicknessType extends AbsenceType {

  @OneToMany(mappedBy = "sicknessType", targetEntity = SicknessRequest.class)
  private List<Request> requests;


}
