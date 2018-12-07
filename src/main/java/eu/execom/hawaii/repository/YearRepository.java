package eu.execom.hawaii.repository;

import eu.execom.hawaii.model.Year;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YearRepository extends JpaRepository<Year, Long> {

  Year findOneByYear(int year);
}
