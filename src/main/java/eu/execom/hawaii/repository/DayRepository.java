package eu.execom.hawaii.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.execom.hawaii.model.Day;
import eu.execom.hawaii.model.Request;

public interface DayRepository extends JpaRepository<Day, Long> {

  List<Day> getDaysByRequest(Request request);
}
