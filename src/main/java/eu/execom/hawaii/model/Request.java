package eu.execom.hawaii.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import eu.execom.hawaii.model.enumerations.RequestStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@ToString(exclude = "days")
@EqualsAndHashCode(callSuper = false)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Request extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -2334871552852759172L;

  @ManyToOne
  private User user;

  @ManyToOne
  private Absence absence;

  @NotNull
  @Enumerated(EnumType.STRING)
  private RequestStatus requestStatus;

  private String reason;

  private LocalDateTime submissionTime;

  @NotNull
  @OneToMany(mappedBy = "request", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<Day> days;

  public boolean isApproved() {
    return RequestStatus.APPROVED.equals(requestStatus);
  }

  public boolean isPending() {
    return RequestStatus.PENDING.equals(requestStatus);
  }

  public boolean isCanceled() {
    return RequestStatus.CANCELED.equals(requestStatus);
  }

  public boolean isCancellationPending() {
    return RequestStatus.CANCELLATION_PENDING.equals(requestStatus);
  }

  public boolean isRejected() {
    return RequestStatus.REJECTED.equals(requestStatus);
  }

}
