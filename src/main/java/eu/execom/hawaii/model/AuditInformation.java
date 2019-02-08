package eu.execom.hawaii.model;

import eu.execom.hawaii.model.enumerations.AuditedEntity;
import eu.execom.hawaii.model.enumerations.OperationPerformed;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class AuditInformation extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1978361670567677720L;

  @Enumerated(EnumType.STRING)
  private AuditedEntity auditedEntity;

  @Enumerated(EnumType.STRING)
  private OperationPerformed operationPerformed;

  private LocalDateTime modifiedDateTime;

  @ManyToOne
  private User modifiedByUser;

  @ManyToOne
  private User modifiedUser;

  private String previousValue;

  private String currentValue;
}
