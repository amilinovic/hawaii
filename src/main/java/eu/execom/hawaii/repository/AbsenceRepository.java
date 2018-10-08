package eu.execom.hawaii.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.execom.hawaii.model.Absence;

import java.util.List;

public interface AbsenceRepository extends JpaRepository<Absence, Long> {
//  List<Absence> findAllActiveAbsences(boolean active);
}
