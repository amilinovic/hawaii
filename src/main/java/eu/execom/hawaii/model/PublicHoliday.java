package eu.execom.hawaii.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class PublicHoliday extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -482913463156061328L;

  private String name;

  private LocalDate date;

  @ManyToOne
  private LeaveProfile leaveProfile;
}
