package eu.execom.hawaii.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class PublicHoliday extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -482913463156061328L;

  @ManyToOne
  private LeaveProfile leaveProfile;

  private boolean active;

  @NotNull
  private String name;

  @NotNull
  private LocalDate date;

}
