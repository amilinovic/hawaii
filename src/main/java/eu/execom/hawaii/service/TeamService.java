package eu.execom.hawaii.service;

import eu.execom.hawaii.model.Team;
import eu.execom.hawaii.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
   * @return a list of all teams.
   */
  public List<Team> findAll() {
    return teamRepository.findAll();
  }

  /**
   * Retrieves a list of all teams from repository by given status.
   *
   * @param deleted is it deleted.
   * @return a list of all teams.
   */
  public List<Team> findAllByDeleted(boolean deleted) {
    return teamRepository.findAllByDeleted(deleted);
  }

  /**
   * Retrieves a team with a specific id.
   *
   * @param id Team id.
   * @return Team with provided id if exists.
   */
  public Team getById(Long id) {
    return teamRepository.getOne(id);
  }

  /**
   * Saves the provided Team to repository.
   *
   * @param team the Team entity to be persisted.
   * @return saved Team.
   */
  @Transactional
  public Team save(Team team) {
    return teamRepository.save(team);
  }

  /**
   * Logically deletes Team.
   *
   * @param id - the team id.
   */
  @Transactional
  public void delete(Long id) {
    var team = getById(id);
    team.setDeleted(true);
    teamRepository.save(team);
  }

}
