package eu.execom.hawaii.repository;

import eu.execom.hawaii.model.Year;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface YearRepository extends JpaRepository<Year, Long> {

  Year findOneByYear(int year);

  boolean existsByYear(int year);

  List<Year> findAllByYearGreaterThanEqual(int year);
}
