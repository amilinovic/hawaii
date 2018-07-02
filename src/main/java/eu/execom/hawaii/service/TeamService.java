package eu.execom.hawaii.service;

import eu.execom.hawaii.model.Team;
import eu.execom.hawaii.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * Team management service.
 */
@Service
public class TeamService {

  private TeamRepository teamRepository;

  @Autowired
  public TeamService(TeamRepository teamRepository) {
    this.teamRepository = teamRepository;
  }

  /**
   * Retrieves a list of all teams from repository.
   *
   * @return a list of all teams
   */
  public List<Team> getAll() {
    return teamRepository.findAll();
  }

  /**
   * Retrieves a team with a specific id.
   * @param id Team id
   * @return Team with provided id if exists
   * @throws EntityNotFoundException if a team with given id is not found
   */
  public Team getById(Long id) {
    return teamRepository.findById(id).orElseThrow(EntityNotFoundException::new);
  }



}
