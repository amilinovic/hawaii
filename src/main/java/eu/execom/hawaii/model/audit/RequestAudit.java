package eu.execom.hawaii.model.audit;

import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.enumerations.AuditedEntity;
import eu.execom.hawaii.model.enumerations.RequestStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class RequestAudit extends Audit {

  private Long id;
  private RequestStatus requestStatus;
  private String reason;
  private LocalDateTime submissionTime;

  private RequestAudit(Request request) {
    this.id = request.getId();
    this.requestStatus = request.getRequestStatus();
    this.reason = request.getReason();
    this.submissionTime = request.getSubmissionTime();
    this.setAuditedEntity(AuditedEntity.REQUEST);
  }

  public static RequestAudit fromRequest(Request request) {
    return new RequestAudit(request);
  }
}
