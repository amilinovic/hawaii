package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.UserRole;
import eu.execom.hawaii.model.enumerations.UserStatusType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDto {

  private Long id;

  @NotNull
  private Long teamId;

  private String teamName;

  @NotNull
  private Long leaveProfileId;

  @NotNull
  private String fullName;

  @NotNull
  private String email;

  @NotNull
  private UserRole userRole;

  private String jobTitle;

  @NotNull
  private UserStatusType userStatusType;

  @NotNull
  private LocalDate startedWorkingDate;

  @NotNull
  private LocalDate startedWorkingAtExecomDate;

  private int yearsOfService;

  private List<UserPushTokenDto> userPushTokens = new ArrayList<>();

  public UserDto(User user) {
    this.id = user.getId();
    this.teamId = (user.getTeam().getId());
    this.teamName = (user.getTeam().getName());
    this.leaveProfileId = (user.getLeaveProfile().getId());
    this.fullName = user.getFullName();
    this.email = user.getEmail();
    this.userRole = user.getUserRole();
    this.jobTitle = user.getJobTitle();
    this.userStatusType = user.getUserStatusType();
    this.startedWorkingDate = user.getStartedWorkingDate();
    this.startedWorkingAtExecomDate = user.getStartedWorkingAtExecomDate();
    this.yearsOfService = user.getYearsOfService();
    this.userPushTokens = user.getUserPushTokens().stream().map(UserPushTokenDto::new).collect(Collectors.toList());
  }

}
