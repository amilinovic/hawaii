package eu.execom.hawaii.dto;

import lombok.Data;

@Data
public class AllowanceForUserDto {

  private int remainingAnnualHours;
  private int nextYearRemainingAnnualHours;
  private int remainingTrainingHours;
}
