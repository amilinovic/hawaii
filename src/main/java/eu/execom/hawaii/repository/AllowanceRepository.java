package eu.execom.hawaii.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.execom.hawaii.model.Allowance;

public interface AllowanceRepository extends JpaRepository<Allowance, Long> {

  Allowance findByUserIdAndYear(Long userId, int year);

}
