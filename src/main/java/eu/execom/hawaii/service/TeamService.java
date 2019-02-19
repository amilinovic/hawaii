package eu.execom.hawaii.service;

import eu.execom.hawaii.model.Team;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.audit.TeamAudit;
import eu.execom.hawaii.model.enumerations.OperationPerformed;
import eu.execom.hawaii.repository.TeamRepository;
import eu.execom.hawaii.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Team management service.
 */
@Service
public class TeamService {

  private TeamRepository teamRepository;

  private AuditInformationService auditInformationService;

  private UserService userService;

  @Autowired
  public TeamService(TeamRepository teamRepository, AuditInformationService auditInformationService,
      UserService userService) {
    this.teamRepository = teamRepository;
    this.userService = userService;
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
   * @param team           the Team entity to be persisted.
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
   * @param team           the Team entity to be persisted.
   * @param modifiedByUser user that made change to Team entity.
   * @return saved Team.
   */
  @Transactional
  public Team update(Team team, User modifiedByUser) {
    var oldTeam = getById(team.getId());
    var users = oldTeam.getUsers();
    team.setUsers(users);
    var previousTeamState = TeamAudit.fromTeam(oldTeam);
    saveAuditInformation(OperationPerformed.UPDATE, modifiedByUser, team, previousTeamState);

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
    var previousTeamState = TeamAudit.fromTeam(team);
    team.setDeleted(true);
    teamRepository.save(team);
    saveAuditInformation(OperationPerformed.DELETE, modifiedByUser, team, previousTeamState);
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
