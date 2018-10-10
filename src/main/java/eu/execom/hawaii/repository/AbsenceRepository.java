package eu.execom.hawaii.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.execom.hawaii.model.Absence;

public interface AbsenceRepository extends JpaRepository<Absence, Long> {
}
