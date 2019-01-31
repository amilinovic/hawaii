package eu.execom.hawaii.api.controller;

import eu.execom.hawaii.dto.TeamDto;
import eu.execom.hawaii.model.Team;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.AuditedEntity;
import eu.execom.hawaii.model.enumerations.OperationPerformed;
import eu.execom.hawaii.service.AuditInformationService;
import eu.execom.hawaii.service.TeamService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

  private TeamService teamService;
  private AuditInformationService auditInformationService;

  @Autowired
  public TeamController(TeamService teamService, AuditInformationService auditInformationService) {
    this.teamService = teamService;
    this.auditInformationService = auditInformationService;
  }

  private static final ModelMapper MAPPER = new ModelMapper();

  @GetMapping
  public ResponseEntity<List<TeamDto>> getTeams(@RequestParam(required = false) Boolean active) {
    var teams = getTeamsByStatus(active);
    var teamDtos = teams.stream().map(TeamDto::new).collect(Collectors.toList());
    return new ResponseEntity<>(teamDtos, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TeamDto> getTeam(@PathVariable Long id) {
    var team = teamService.getById(id);
    var teamDto = new TeamDto(team);
    return new ResponseEntity<>(teamDto, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<TeamDto> createTeam(@ApiIgnore @AuthenticationPrincipal User authUser,
      @RequestBody TeamDto teamDto) {
    var team = MAPPER.map(teamDto, Team.class);
    team = teamService.save(team);
    sendAuditInformation(OperationPerformed.CREATE, authUser, team.getUsers(), null, teamDto);

    return new ResponseEntity<>(new TeamDto(team), HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<TeamDto> updateTeam(@ApiIgnore @AuthenticationPrincipal User authUser,
      @RequestBody TeamDto teamDto) {
    var oldTeam = teamService.getById(teamDto.getId());
    var team = MAPPER.map(teamDto, Team.class);
    sendAuditInformation(OperationPerformed.UPDATE, authUser, oldTeam.getUsers(), new TeamDto(oldTeam), teamDto);
    team = teamService.save(team);

    return new ResponseEntity<>(new TeamDto(team), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity deleteTeam(@ApiIgnore @AuthenticationPrincipal User authUser, @PathVariable Long id) {
    var oldTeam = teamService.getById(id);
    TeamDto team = new TeamDto(oldTeam);
    team.setDeleted(true);
    sendAuditInformation(OperationPerformed.DELETE, authUser, oldTeam.getUsers(), new TeamDto(oldTeam), team);
    teamService.delete(id);

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  private void sendAuditInformation(OperationPerformed operationPerformed, User authUser, List<User> users,
      TeamDto previousState, TeamDto currentState) {

    if (users.isEmpty()) {
      auditInformationService.buildAuditInformation(operationPerformed, AuditedEntity.TEAM, authUser, null,
          previousState, currentState);
    } else {
      users.forEach(
          user -> auditInformationService.buildAuditInformation(operationPerformed, AuditedEntity.TEAM, authUser, user,
              previousState, currentState));
    }
  }

  private List<Team> getTeamsByStatus(Boolean deleted) {
    return deleted == null ? teamService.findAll() : teamService.findAllByDeleted(deleted);
  }
}
