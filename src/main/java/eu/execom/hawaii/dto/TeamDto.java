package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.Team;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TeamDto {

  private Long id;
  private String name;
  private String sicknessRequestEmails;
  private String annualRequestEmails;
  private String bonusRequestEmails;
  private boolean sendEmailToTeammatesForSicknessRequestEnabled;
  private boolean sendEmailToTeammatesForAnnualRequestEnabled;
  private boolean sendEmailToTeammatesForBonusRequestEnabled;
  private boolean deleted;
  private List<UserDto> users = new ArrayList<>();
  private List<UserDto> teamApprovers = new ArrayList<>();

  public TeamDto(Team team) {
    this.id = team.getId();
    this.name = team.getName();
    this.sicknessRequestEmails = team.getSicknessRequestEmails();
    this.annualRequestEmails = team.getAnnualRequestEmails();
    this.bonusRequestEmails = team.getBonusRequestEmails();
    this.sendEmailToTeammatesForSicknessRequestEnabled = team.isSendEmailToTeammatesForSicknessRequestEnabled();
    this.sendEmailToTeammatesForAnnualRequestEnabled = team.isSendEmailToTeammatesForAnnualRequestEnabled();
    this.sendEmailToTeammatesForBonusRequestEnabled = team.isSendEmailToTeammatesForBonusRequestEnabled();
    this.deleted = team.isDeleted();
    this.users = team.getUsers().stream().map(UserDto::new).collect(Collectors.toList());
    this.teamApprovers = team.getTeamApprovers().stream().map(UserDto::new).collect(Collectors.toList());
  }
}
