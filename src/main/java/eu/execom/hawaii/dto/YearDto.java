package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.Year;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class YearDto {

  private Long yearId;
  private int year;
  private List<AllowanceDto> allowances;
  private boolean active;

  public YearDto(Year year) {
    this.yearId = year.getId();
    this.year = year.getYear();
    this.allowances = year.getAllowances().stream().map(AllowanceDto::new).collect(Collectors.toList());
    this.active = year.isActive();
  }
}
