package eu.execom.hawaii.model.audit;

import eu.execom.hawaii.model.enumerations.AuditedEntity;
import lombok.Data;

import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public abstract class Audit {

  private AuditedEntity auditedEntity;
}
