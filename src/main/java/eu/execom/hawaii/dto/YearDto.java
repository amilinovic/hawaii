package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.Year;
import lombok.Data;

@Data
public class YearDto {

  private Long yearId;
  private int year;
  private boolean active;

//  private List<Allowance> allowances;

  public YearDto(Year year){
    this.yearId = year.getId();
    this.year = year.getYear();
    this.active = year.isActive();
//    this.allowances = year.getAllowances();
  }
}
