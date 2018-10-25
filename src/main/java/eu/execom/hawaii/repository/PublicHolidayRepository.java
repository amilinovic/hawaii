package eu.execom.hawaii.repository;

import eu.execom.hawaii.model.PublicHoliday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PublicHolidayRepository extends JpaRepository<PublicHoliday, Long> {

  List<PublicHoliday> findAllByActive(boolean active);

  List<PublicHoliday> findAllByDateIsBetween(LocalDate from, LocalDate to);

}
