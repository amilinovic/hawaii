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
  private String emails;
  private boolean deleted;

  private TeamAudit(Team team) {
    this.setAuditedEntity(AuditedEntity.TEAM);
    this.id = team.getId();
    this.teamName = team.getName();
    this.emails = team.getEmails();
    this.deleted = team.isDeleted();
  }

  public static TeamAudit createTeamAuditEntity(Team team) {
    return new TeamAudit(team);
  }
}
