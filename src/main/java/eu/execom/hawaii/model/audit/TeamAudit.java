package eu.execom.hawaii.model.audit;

import eu.execom.hawaii.model.Team;
import eu.execom.hawaii.model.enumerations.AuditedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TeamAudit extends Audit {

  private Long id;
  private String teamName;
  private String sicknessRequestEmails;
  private String annualRequestEmails;
  private String bonusRequestEmails;
  private boolean sendEmailToTeammatesForSicknessRequestEnabled;
  private boolean sendEmailToTeammatesForAnnualRequestEnabled;
  private boolean sendEmailToTeammatesForBonusRequestEnabled;
  private boolean deleted;

  private TeamAudit(Team team) {
    this.id = team.getId();
    this.teamName = team.getName();
    this.sicknessRequestEmails = team.getSicknessRequestEmails();
    this.annualRequestEmails = team.getAnnualRequestEmails();
    this.bonusRequestEmails = team.getBonusRequestEmails();
    this.sendEmailToTeammatesForSicknessRequestEnabled = team.isSendEmailToTeammatesForSicknessRequestEnabled();
    this.sendEmailToTeammatesForAnnualRequestEnabled = team.isSendEmailToTeammatesForAnnualRequestEnabled();
    this.sendEmailToTeammatesForBonusRequestEnabled = team.isSendEmailToTeammatesForBonusRequestEnabled();
    this.deleted = team.isDeleted();
    this.setAuditedEntity(AuditedEntity.TEAM);
  }

  public static TeamAudit fromTeam(Team team) {
    return new TeamAudit(team);
  }
}
