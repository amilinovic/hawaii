package eu.execom.hawaii.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

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

  private int year;

  private int annual;

  private int takenAnnual;

  private int sickness;

  private int bonus;

  private int takenBonus;

  private int carriedOver;

  private int manualAdjust;

  private int training;

}
