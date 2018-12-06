package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.UserRole;
import eu.execom.hawaii.model.enumerations.UserStatusType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDto {

  private Long id;
  private Long teamId;
  private String teamName;
  private Long leaveProfileId;
  private String fullName;
  private String email;
  private UserRole userRole;
  private String jobTitle;
  private UserStatusType userStatusType;
  private int yearsOfService;
  private List<AllowanceDto> allowances = new ArrayList<>();
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
    this.yearsOfService = user.getYearsOfService();
    this.allowances = user.getAllowances().stream().map(AllowanceDto::new).collect(Collectors.toList());
    this.userPushTokens = user.getUserPushTokens().stream().map(UserPushTokenDto::new).collect(Collectors.toList());
  }

}
