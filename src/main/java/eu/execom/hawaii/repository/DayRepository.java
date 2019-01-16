package eu.execom.hawaii.repository;

import eu.execom.hawaii.model.Day;
import eu.execom.hawaii.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DayRepository extends JpaRepository<Day, Long> {

  Day findFirstByOrderByDateAsc();

  Day findFirstByOrderByDateDesc();

  List<Day> findAllByRequestInAndDateIsBetween(List<Request> requests, LocalDate startDate, LocalDate endDate);

}
