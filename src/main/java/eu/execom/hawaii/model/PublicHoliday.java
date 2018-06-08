package eu.execom.hawaii.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class PublicHoliday {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  private LocalDate date;

  @ManyToOne
  private LeaveProfile leaveProfile;
}
