package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.Year;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class YearDto {

  private Long id;

  @NotNull
  private int year;

  @NotNull
  private boolean active;

  private List<AllowanceWithoutYearDto> allowances;

  public YearDto(Year year) {
    this.id = year.getId();
    this.year = year.getYear();
    this.allowances = year.getAllowances().stream().map(AllowanceWithoutYearDto::new).collect(Collectors.toList());
    this.active = year.isActive();
  }
}
