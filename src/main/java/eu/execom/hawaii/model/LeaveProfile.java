package eu.execom.hawaii.model;

import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class LeaveProfile {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  private String comment;

  private Map<Long, LeaveProfileDetail> leaveProfileDetails;

  private List<PublicHoliday> publicHolidays;

}
