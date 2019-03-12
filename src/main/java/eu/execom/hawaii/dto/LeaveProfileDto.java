package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.LeaveProfile;
import eu.execom.hawaii.model.enumerations.LeaveProfileType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class LeaveProfileDto {

  private Long id;

  @NotNull
  private String name;

  @NotNull
  private int entitlement;

  @NotNull
  private int maxCarriedOver;

  @NotNull
  private int maxBonusDays;

  @NotNull
  private int maxAllowanceFromNextYear;

  @NotNull
  private int training;

  @NotNull
  private boolean deleted;

  @NotNull
  private boolean upgradeable;

  @NotNull
  private LeaveProfileType leaveProfileType;

  private String comment;

  private List<UserDto> users = new ArrayList<>();

  public LeaveProfileDto(LeaveProfile leaveProfile) {
    this.id = leaveProfile.getId();
    this.name = leaveProfile.getName();
    this.entitlement = leaveProfile.getEntitlement();
    this.maxCarriedOver = leaveProfile.getMaxCarriedOver();
    this.maxBonusDays = leaveProfile.getMaxBonusDays();
    this.maxAllowanceFromNextYear = leaveProfile.getMaxAllowanceFromNextYear();
    this.training = leaveProfile.getTraining();
    this.deleted = leaveProfile.isDeleted();
    this.upgradeable = leaveProfile.isUpgradeable();
    this.leaveProfileType = leaveProfile.getLeaveProfileType();
    this.comment = leaveProfile.getComment();
    this.users = leaveProfile.getUsers().stream().map(UserDto::new).collect(Collectors.toList());
  }

}
