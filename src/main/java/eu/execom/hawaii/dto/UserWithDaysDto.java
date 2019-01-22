package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.Day;
import eu.execom.hawaii.model.User;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserWithDaysDto {

  private Long id;
  private Long teamId;
  private String teamName;
  private String fullName;
  private String email;
  private String jobTitle;
  private List<DayDto> days;

  public UserWithDaysDto(User user, List<Day> days) {
    this.id = user.getId();
    this.teamId = user.getTeam().getId();
    this.teamName = user.getTeam().getName();
    this.fullName = user.getFullName();
    this.email = user.getEmail();
    this.jobTitle = user.getJobTitle();
    this.days = days.stream().map(DayDto::new).collect(Collectors.toList());

  }

}
