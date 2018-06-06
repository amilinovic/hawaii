package eu.execom.hawaii.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import eu.execom.hawaii.model.enumerations.Duration;
import lombok.Data;

@Entity
@Data
public class Day implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "request_id")
  private Request request;

  private LocalDate date;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Duration duration;
}
