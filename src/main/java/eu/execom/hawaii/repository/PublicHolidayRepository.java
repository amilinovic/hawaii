package eu.execom.hawaii.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.execom.hawaii.model.PublicHoliday;

public interface PublicHolidayRepository extends JpaRepository<PublicHoliday, Long> {

}
