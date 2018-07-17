package eu.execom.hawaii.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.execom.hawaii.model.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {

  List<Team> findAllByActive(boolean active);
}
