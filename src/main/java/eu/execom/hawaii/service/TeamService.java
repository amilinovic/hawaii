package eu.execom.hawaii.service;

import eu.execom.hawaii.exceptions.ActionNotAllowedException;
import eu.execom.hawaii.exceptions.NotAuthorizedApprovalException;
import eu.execom.hawaii.model.Team;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.audit.TeamAudit;
import eu.execom.hawaii.model.enumerations.OperationPerformed;
import eu.execom.hawaii.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
   * Makes audit of that save.
   *
   * @param team the Team entity to be persisted.
   * @param modifiedByUser user that made changes to that Team entity.
   * @return saved Team.
   */
  @Transactional
  public Team save(Team team, User modifiedByUser) {
    saveAuditInformation(OperationPerformed.CREATE, modifiedByUser, team, null);
    return teamRepository.save(team);
  }

  /**
   * Saves the provided Team to repository.
   *
   * @param team the Team entity to be persisted.
   * @param modifiedByUser user that made change to Team entity.
   * @return saved Team.
   */
  @Transactional
  public Team update(Team team, User modifiedByUser) {
    var oldTeam = getById(team.getId());
    for (User u : team.getUsers()) {
      u.setTeam(team);
    }
    var previousTeamState = TeamAudit.fromTeam(oldTeam);
    saveAuditInformation(OperationPerformed.UPDATE, modifiedByUser, team, previousTeamState);

    return teamRepository.save(team);
  }

  /**
   * Deletes Team from database if its member list is empty.
   *
   * @param modifiedByUser user that made change to Team entity.
   * @param id - the team id.
   */
  @Transactional
  public void delete(Long id, User modifiedByUser) {
    var team = getById(id);
    var previousTeamState = TeamAudit.fromTeam(team);
    if (team.getUsers().isEmpty()) {
      teamRepository.deleteById(id);
      saveAuditInformation(OperationPerformed.DELETE, modifiedByUser, team, previousTeamState);
    } else {
      throw new ActionNotAllowedException("Team member list needs to be empty");
    }

  }

  private void saveAuditInformation(OperationPerformed operationPerformed, User modifiedByUser, Team team,
      TeamAudit previousTeamState) {
    var currentTeamState = TeamAudit.fromTeam(team);

    if (team.getUsers().isEmpty()) {
      auditInformationService.saveAudit(operationPerformed, modifiedByUser, null, previousTeamState, currentTeamState);
    } else {
      team.getUsers()
          .forEach(modifiedUser -> auditInformationService.saveAudit(operationPerformed, modifiedByUser, modifiedUser,
              previousTeamState, currentTeamState));
    }
  }

}
