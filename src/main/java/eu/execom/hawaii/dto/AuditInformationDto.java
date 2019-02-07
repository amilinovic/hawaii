package eu.execom.hawaii.dto;

import eu.execom.hawaii.model.AuditInformation;
import eu.execom.hawaii.model.enumerations.AuditedEntity;
import eu.execom.hawaii.model.enumerations.OperationPerformed;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditInformationDto {

  private Long auditInformationId;
  private AuditedEntity auditedEntity;
  private OperationPerformed operationPerformed;
  private UserDto modifiedByUserId;
  private Long modifiedUserId;
  private String previousValue;
  private String currentValue;

  private LocalDateTime modifiedDateTime;

  public AuditInformationDto(AuditInformation auditInformation) {
    this.auditInformationId = auditInformation.getId();
    this.auditedEntity = auditInformation.getAuditedEntity();
    this.operationPerformed = auditInformation.getOperationPerformed();
    this.modifiedByUserId = new UserDto(auditInformation.getModifiedByUser());
    this.modifiedUserId = auditInformation.getModifiedUser().getId();
    this.previousValue = auditInformation.getPreviousValue();
    this.currentValue = auditInformation.getCurrentValue();
    this.modifiedDateTime = auditInformation.getModifiedDateTime();
  }

}
