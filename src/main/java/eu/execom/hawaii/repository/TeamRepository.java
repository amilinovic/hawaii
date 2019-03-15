package eu.execom.hawaii.repository;

import eu.execom.hawaii.model.Team;

public interface TeamRepository extends CustomizedTeamRepository<Team, Long> {

  void deleteById(Long id);

  boolean existsByName(String name);

  Team findByName(String name);

}
