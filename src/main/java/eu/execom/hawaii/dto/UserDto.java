package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.UserRole;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDto {

  private Long id;
  private String teamName;
  private Long leaveProfileId;
  private String fullName;
  private String email;
  private UserRole userRole;
  private String jobTitle;
  private boolean active;
  private int yearsOfService;
  private List<AllowanceDto> allowances = new ArrayList<>();

  public UserDto(User user) {
    this.id = user.getId();
    this.teamName = (user.getTeam().getName());
    this.leaveProfileId = (user.getLeaveProfile().getId());
    this.fullName = user.getFullName();
    this.email = user.getEmail();
    this.userRole = user.getUserRole();
    this.jobTitle = user.getJobTitle();
    this.active = user.isActive();
    this.yearsOfService = user.getYearsOfService();
    this.allowances = user.getAllowances().stream().map(AllowanceDto::new).collect(Collectors.toList());
  }

}
