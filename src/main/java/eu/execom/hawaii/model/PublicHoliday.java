package eu.execom.hawaii.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class PublicHoliday extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -482913463156061328L;

  @NotNull
  private boolean deleted;

  @NotNull
  @Column(unique = true)
  private String name;

  @NotNull
  private LocalDate date;

}
