package eu.execom.hawaii.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class Allowance implements Serializable {

  private static final long serialVersionUID = 4288980454836980764L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private int year;

  private int annual;

  private int sick;

  private int training;

  private int carriedOver;

  private int bonus;

  private int manualAdjust;

  @ManyToOne
  private User user;

}
