package eu.execom.hawaii.api.controller;

import eu.execom.hawaii.dto.TeamDto;
import eu.execom.hawaii.model.Team;
import eu.execom.hawaii.model.User;
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

  @Autowired
  public TeamController(TeamService teamService) {
    this.teamService = teamService;
  }

  private static final ModelMapper MAPPER = new ModelMapper();

  @GetMapping
  public ResponseEntity<List<TeamDto>> getTeams() {
    var teams = teamService.findAll();
    var teamDtos = teams.stream().map(TeamDto::new).collect(Collectors.toList());
    return new ResponseEntity<>(teamDtos, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TeamDto> getTeam(@PathVariable Long id) {
    var team = teamService.getById(id);
    var teamDto = new TeamDto(team);
    return new ResponseEntity<>(teamDto, HttpStatus.OK);
  }

  @GetMapping("/search")
  public ResponseEntity<List<TeamDto>> searchTeamsByUsersNameContaining(@RequestParam String fullNameQuery) {
    List<Team> teams = teamService.searchTeamsByUsersNameContaining(fullNameQuery);
    var teamDtos = teams.stream().map(TeamDto::new).collect(Collectors.toList());
    return new ResponseEntity<>(teamDtos, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<TeamDto> createTeam(@ApiIgnore @AuthenticationPrincipal User authUser,
      @RequestBody TeamDto teamDto) {
    var team = MAPPER.map(teamDto, Team.class);
    team = teamService.create(team, authUser);

    return new ResponseEntity<>(new TeamDto(team), HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<TeamDto> updateTeam(@ApiIgnore @AuthenticationPrincipal User authUser,
      @RequestBody TeamDto teamDto) {
    var team = MAPPER.map(teamDto, Team.class);
    team = teamService.update(team, authUser);

    return new ResponseEntity<>(new TeamDto(team), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity deleteTeam(@ApiIgnore @AuthenticationPrincipal User authUser, @PathVariable Long id) {
    teamService.delete(id, authUser);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }
}
