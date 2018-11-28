package eu.execom.hawaii.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class PublicHoliday extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -482913463156061328L;

  private boolean deleted;

  @NotNull
  private String name;

  @NotNull
  private LocalDate date;

}
