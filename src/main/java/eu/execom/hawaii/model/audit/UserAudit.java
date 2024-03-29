package eu.execom.hawaii.model.audit;

import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.AuditedEntity;
import eu.execom.hawaii.model.enumerations.UserRole;
import eu.execom.hawaii.model.enumerations.UserStatusType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserAudit extends Audit {

  private Long id;
  private Long teamId;
  private Long leaveProfileId;
  private String fullName;
  private String email;
  private UserRole userRole;
  private String jobTitle;
  private UserStatusType userStatusType;
  private LocalDate startedWorkingDate;
  private LocalDate startedWorkingAtExecomDate;
  private LocalDate stoppedWorkingAtExecomDate;

  private UserAudit(User user) {
    this.id = user.getId();
    this.teamId = user.getTeam().getId();
    this.leaveProfileId = user.getLeaveProfile().getId();
    this.fullName = user.getFullName();
    this.email = user.getEmail();
    this.userRole = user.getUserRole();
    this.jobTitle = user.getJobTitle();
    this.userStatusType = user.getUserStatusType();
    this.startedWorkingDate = user.getStartedWorkingDate();
    this.startedWorkingAtExecomDate = user.getStartedWorkingAtExecomDate();
    this.stoppedWorkingAtExecomDate = user.getStoppedWorkingAtExecomDate();
    this.setAuditedEntity(AuditedEntity.USER);
  }

  public static UserAudit fromUser(User user) {
    return new UserAudit(user);
  }
}
