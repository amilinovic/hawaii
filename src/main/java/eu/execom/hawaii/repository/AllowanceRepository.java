package eu.execom.hawaii.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.execom.hawaii.model.Allowance;

import java.util.List;

public interface AllowanceRepository extends JpaRepository<Allowance, Long> {

  Allowance findByUserIdAndYearYear(Long userId, int year);
  List<Allowance> findAllByUserId(Long userId);
  boolean existsByUserIdAndYearYear(Long userId, int year);

}
