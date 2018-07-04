package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.LeaveProfile;

public class LeaveProfileDto {

  private Long id;
  private String name;
  private int entitlement;
  private int maxCarriedOver;
  private int training;
  private String comment;

  public LeaveProfileDto(LeaveProfile leaveProfile) {
    this.id = leaveProfile.getId();
    this.name = leaveProfile.getName();
    this.entitlement = leaveProfile.getEntitlement();
    this.maxCarriedOver = leaveProfile.getMaxCarriedOver();
    this.training = leaveProfile.getTraining();
    this.comment = leaveProfile.getComment();
  }

}
