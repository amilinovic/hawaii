package eu.execom.hawaii.repository;

import eu.execom.hawaii.model.PublicHoliday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PublicHolidayRepository extends JpaRepository<PublicHoliday, Long> {

  List<PublicHoliday> findAllByDeleted(boolean deleted);

  List<PublicHoliday> findAllByDateIsBetween(LocalDate from, LocalDate to);

}
