package eu.execom.hawaii.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.execom.hawaii.model.Allowance;
import eu.execom.hawaii.model.User;

public interface AllowanceRepository extends JpaRepository<Allowance, Long> {

  Allowance findByUser(User user);

}
