package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.Team;
import lombok.Data;

@Data
public class TeamDto {

  private Long id;
  private String name;
  private String emails;
  private boolean active;

  public TeamDto(Team team) {
    this.id = team.getId();
    this.name = team.getName();
    this.emails = team.getEmails();
    this.active = team.isActive();
  }
}
