package eu.execom.hawaii.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eu.execom.hawaii.model.LeaveProfile;
import lombok.Data;

@Data
public class LeaveProfileDto {

  private Long id;
  private String name;
  private int entitlement;
  private int maxCarriedOver;
  private int training;
  private String comment;
  private List<UserDto> users = new ArrayList<>();

  public LeaveProfileDto(LeaveProfile leaveProfile) {
    this.id = leaveProfile.getId();
    this.name = leaveProfile.getName();
    this.entitlement = leaveProfile.getEntitlement();
    this.maxCarriedOver = leaveProfile.getMaxCarriedOver();
    this.training = leaveProfile.getTraining();
    this.comment = leaveProfile.getComment();
    this.users = leaveProfile.getUsers().stream().map(UserDto::new).collect(Collectors.toList());
  }

}
