package eu.execom.hawaii.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.execom.hawaii.model.Team;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TeamDto {

  private Long id;
  private String name;
  private String emails;
  private boolean deleted;

  @JsonIgnore
  private List<UserDto> users = new ArrayList<>();

  private List<UserDto> teamApprovers = new ArrayList<>();

  public TeamDto(Team team) {
    this.id = team.getId();
    this.name = team.getName();
    this.emails = team.getEmails();
    this.deleted = team.isDeleted();
    this.users = team.getUsers().stream().map(UserDto::new).collect(Collectors.toList());
    this.teamApprovers = team.getTeamApprovers().stream().map(UserDto::new).collect(Collectors.toList());
  }
}
