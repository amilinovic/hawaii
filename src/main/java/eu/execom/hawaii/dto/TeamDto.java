package eu.execom.hawaii.dto;

import java.util.List;
import java.util.stream.Collectors;

import eu.execom.hawaii.model.Team;
import lombok.Data;

@Data
public class TeamDto {

  private Long id;
  private String name;
  private String emails;
  private boolean active;
  private List<UserDto> users;
  private List<UserDto> teamApprovers;

  public TeamDto(Team team) {
    this.id = team.getId();
    this.name = team.getName();
    this.emails = team.getEmails();
    this.active = team.isActive();
    this.users = team.getUsers().stream().map(UserDto::new).collect(Collectors.toList());
    this.teamApprovers = team.getTeamApprovers().stream().map(UserDto::new).collect(Collectors.toList());
  }
}
