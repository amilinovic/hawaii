package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.Allowance;
import lombok.Data;

@Data
public class AllowanceDto {

  private Long userId;
  private int year;
  private int annual;
  private int taken;
  private int sickness;
  private int bonus;
  private int carriedOver;
  private int manualAdjust;

  public AllowanceDto (Allowance allowance) {
    this.userId = allowance.getUser().getId();
    this.year = allowance.getYear();
    this.annual = allowance.getAnnual();
    this.taken = allowance.getTaken();
    this.sickness = allowance.getSickness();
    this.bonus = allowance.getBonus();
    this.carriedOver = allowance.getCarriedOver();
    this.manualAdjust = allowance.getManualAdjust();
  }

}
