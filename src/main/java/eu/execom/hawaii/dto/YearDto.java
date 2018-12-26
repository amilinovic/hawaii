package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.Allowance;
import eu.execom.hawaii.model.Year;
import lombok.Data;

import java.util.List;

@Data
public class YearDto {

  private Long yearId;
  private int year;
  private List<Allowance> allowances;
  private boolean active;

  public YearDto(Year year) {
    this.yearId = year.getId();
    this.year = year.getYear();
    this.allowances = year.getAllowances();
    this.active = year.isActive();
  }
}
