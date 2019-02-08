package eu.execom.hawaii.repository;

import eu.execom.hawaii.model.AuditInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditInformationRepository extends JpaRepository<AuditInformation, Long> {

}
