package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.Allowance;
import lombok.Data;

@Data
public class AllowanceDto {

  private Long userId;
  private int year;
  private int annual;
  private int takenAnnual;
  private int sickness;
  private int bonus;
  private int carriedOver;
  private int manualAdjust;
  private int training;
  private int takenTraining;

  public AllowanceDto(Allowance allowance) {
    this.userId = allowance.getUser().getId();
    this.year = allowance.getYear();
    this.annual = allowance.getAnnual();
    this.takenAnnual = allowance.getTakenAnnual();
    this.sickness = allowance.getSickness();
    this.bonus = allowance.getBonus();
    this.carriedOver = allowance.getCarriedOver();
    this.manualAdjust = allowance.getManualAdjust();
    this.training = allowance.getTraining();
    this.takenTraining = allowance.getTakenTraining();
  }

}
