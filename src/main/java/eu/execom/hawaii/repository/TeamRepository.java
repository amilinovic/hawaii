package eu.execom.hawaii.repository;

import eu.execom.hawaii.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

  List<Team> findAllByDeleted(boolean deleted);

}
