package eu.execom.hawaii.repository;

import eu.execom.hawaii.model.Absence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbsenceRepository extends JpaRepository<Absence, Long> {
}
