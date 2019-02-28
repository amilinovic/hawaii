package eu.execom.hawaii.service;

import eu.execom.hawaii.exceptions.ActionNotAllowedException;
import eu.execom.hawaii.model.Team;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.audit.TeamAudit;
import eu.execom.hawaii.model.enumerations.OperationPerformed;
import eu.execom.hawaii.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Team management service.
 */
@Slf4j
@Service
public class TeamService {

  private static final Long TEMPORARY_TEAM_ID = -1L;

  private TeamRepository teamRepository;
  private AuditInformationService auditInformationService;
  private UserService userService;

  @Autowired
  public TeamService(TeamRepository teamRepository, AuditInformationService auditInformationService,
      UserService userService) {
    this.teamRepository = teamRepository;
    this.auditInformationService = auditInformationService;
    this.userService = userService;
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
   * Retrieves a list of teams from repository by given users fullName query.
   *
   * @param fullNameQuery User fullName query
   * @return a list of all teams where user with given fullName query is member or teamApprover.
   */
  public List<Team> searchTeamsByUsersNameContaining(String fullNameQuery) {

    List<User> users = userService.findByFullNameContaining(fullNameQuery);

    List<Team> teams = new ArrayList<>();
    for (User u : users) {
      teams.addAll(u.getApproverTeams());
      teams.add(u.getTeam());
    }

    return teams;
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
  public Team create(Team team, User modifiedByUser) {
    saveAuditInformation(OperationPerformed.CREATE, modifiedByUser, team, null);
    if (team.getId() == null) {
      team.setId(TEMPORARY_TEAM_ID);
    }
    team.getUsers().forEach(user -> user.setTeam(team));

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
      log.error("Team: {}, still contains {} members, team needs to be empty before it can be deleted.", team.getName(),
          team.getUsers().size());
      throw new ActionNotAllowedException();
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
