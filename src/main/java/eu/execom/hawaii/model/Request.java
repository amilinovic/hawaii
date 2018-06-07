package eu.execom.hawaii.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import eu.execom.hawaii.model.enumerations.RequestStatus;
import eu.execom.hawaii.model.enumerations.RequestType;
import lombok.Data;

@Entity
@Data
public class Request implements Serializable {

  private static final long serialVersionUID = -2334871552852759172L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @NotNull
  @Enumerated(EnumType.STRING)
  private RequestType requestType;

  @NotNull
  @Enumerated(EnumType.STRING)
  private RequestStatus requestStatus;

  private String reason;

  @OneToMany(mappedBy = "request")
  private List<Day> days;

  @ManyToOne
  private User approvedBy;

}
