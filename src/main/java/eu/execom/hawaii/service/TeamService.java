package eu.execom.hawaii.service;

import eu.execom.hawaii.model.Team;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.audit.TeamAudit;
import eu.execom.hawaii.model.enumerations.OperationPerformed;
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
  private AuditInformationService auditInformationService;

  @Autowired
  public TeamService(TeamRepository teamRepository, AuditInformationService auditInformationService) {
    this.teamRepository = teamRepository;
    this.auditInformationService = auditInformationService;
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
  public Team save(Team team, User modifiedByUser, OperationPerformed operationPerformed) {
    if (operationPerformed.equals(OperationPerformed.CREATE)) {
      saveAuditInformation(operationPerformed, modifiedByUser, team, null);
    } else {
      var oldTeam = getById(team.getId());
      var users = oldTeam.getUsers();
      team.setUsers(users);
      var previousTeamState = TeamAudit.createTeamAuditEntity(oldTeam);
      saveAuditInformation(operationPerformed, modifiedByUser, team, previousTeamState);
    }

    return teamRepository.save(team);
  }

  /**
   * Logically deletes Team.
   *
   * @param id - the team id.
   */
  @Transactional
  public void delete(Long id, User modifiedByUser) {
    var team = getById(id);
    var previousTeamState = TeamAudit.createTeamAuditEntity(team);
    team.setDeleted(true);
    saveAuditInformation(OperationPerformed.DELETE, modifiedByUser, team, previousTeamState);
    teamRepository.save(team);
  }

  public void saveAuditInformation(OperationPerformed operationPerformed, User modifiedByUser, Team team,
      TeamAudit previousTeamState) {
    var currentTeamState = TeamAudit.createTeamAuditEntity(team);

    if (team.getUsers().isEmpty()) {
      auditInformationService.saveAudit(operationPerformed, modifiedByUser, null, previousTeamState, currentTeamState);
    } else {
      team.getUsers()
          .forEach(modifiedUser -> auditInformationService.saveAudit(operationPerformed, modifiedByUser, modifiedUser,
              previousTeamState, currentTeamState));
    }
  }

}
