package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.Allowance;
import lombok.Data;

@Data
public class AllowanceWithoutYearDto {

  private Long userId;
  private int takenAnnual;
  private int pendingAnnual;
  private int annual;
  private int sickness;
  private int bonus;
  private int carriedOver;
  private int manualAdjust;
  private int takenTraining;
  private int pendingTraining;
  private int training;
  private int takenInPreviousYear;
  private int pendingInPreviousYear;

  public AllowanceWithoutYearDto(Allowance allowance) {
    this.userId = allowance.getUser().getId();
    this.annual = allowance.getAnnual();
    this.takenAnnual = allowance.getTakenAnnual();
    this.pendingAnnual = allowance.getPendingAnnual();
    this.sickness = allowance.getSickness();
    this.bonus = allowance.getBonus();
    this.carriedOver = allowance.getCarriedOver();
    this.manualAdjust = allowance.getManualAdjust();
    this.training = allowance.getTraining();
    this.takenTraining = allowance.getTakenTraining();
    this.pendingTraining = allowance.getPendingTraining();
    this.takenInPreviousYear = allowance.getTakenInPreviousYear();
    this.pendingInPreviousYear = allowance.getPendingInPreviousYear();
  }
}
