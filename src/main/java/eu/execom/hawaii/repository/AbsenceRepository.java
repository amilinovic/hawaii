package eu.execom.hawaii.repository;

import eu.execom.hawaii.model.absence.Absence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbsenceRepository extends JpaRepository<Absence, Long> {
}
