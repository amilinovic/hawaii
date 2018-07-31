package eu.execom.hawaii.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.execom.hawaii.model.Team;
import eu.execom.hawaii.model.TeamApprover;

public interface TeamApproverRepository extends JpaRepository<TeamApprover, Long> {

  void deleteTeamApproversByTeam(Team team);

}
