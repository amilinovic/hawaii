package eu.execom.hawaii.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.execom.hawaii.model.PublicHoliday;

public interface PublicHolidayRepository extends JpaRepository<PublicHoliday, Long> {

  List<PublicHoliday> findAllByActive(boolean active);

  List<PublicHoliday> findAllByDateIsBetween(LocalDate from, LocalDate to);

}
