package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.UserRole;
import lombok.Data;

@Data
public class UserDto {

  private Long id;
  private Long teamId;
  private Long leaveProfileId;
  private String fullName;
  private String email;
  private UserRole userRole;
  private String jobTitle;
  private boolean active;

  public UserDto(User user) {
    this.id = user.getId();
    this.teamId = (user.getTeam().getId());
    this.leaveProfileId = (user.getLeaveProfile().getId());
    this.fullName = user.getFullName();
    this.email = user.getEmail();
    this.userRole = user.getUserRole();
    this.jobTitle = user.getJobTitle();
    this.active = user.isActive();
  }

}
