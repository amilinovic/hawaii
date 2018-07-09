package eu.execom.hawaii.repository;

import eu.execom.hawaii.model.absence.Leave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
}
