package eu.execom.hawaii.repository;

import eu.execom.hawaii.model.Day;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DayRepository extends JpaRepository<Day, Long> {

  Day findFirstByOrderByDateAsc();

  Day findFirstByOrderByDateDesc();

}
