package eu.execom.hawaii.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * All integer field are represented in hour, beside year field.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Allowance extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 4288980454836980764L;

  @ManyToOne
  private User user;

  @ManyToOne
  private Year year;
  //  private int year;

  private int annual;

  private int takenAnnual;

  private int pendingAnnual;

  private int sickness;

  private int bonus;

  private int carriedOver;

  private int manualAdjust;

  private int training;

  private int takenTraining;

  private int pendingTraining;
}
