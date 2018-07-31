package eu.execom.hawaii.dto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import eu.execom.hawaii.model.Team;
import eu.execom.hawaii.model.TeamApprover;
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
    this.users = team.getUsers() == null ?
        Collections.emptyList() :
        team.getUsers().stream().map(UserDto::new).collect(Collectors.toList());
    this.teamApprovers = team.getTeamApprovers() == null ?
        Collections.emptyList() :
        team.getTeamApprovers().stream().map(TeamApprover::getUser).map(UserDto::new).collect(Collectors.toList());
  }
}
