package eu.execom.hawaii.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.execom.hawaii.model.Day;

public interface DayRepository extends JpaRepository<Day, Long> {

}
