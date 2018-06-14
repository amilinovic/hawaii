package eu.execom.hawaii.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import eu.execom.hawaii.model.enumerations.Duration;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Day extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -8942661964026176956L;

  @ManyToOne
  @JoinColumn(name = "request_id")
  @NotNull
  private Request request;

  @NotNull
  private LocalDate date;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Duration duration;

}
