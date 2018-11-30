package eu.execom.hawaii.api.controller;

import eu.execom.hawaii.dto.TeamDto;
import eu.execom.hawaii.model.Team;
import eu.execom.hawaii.service.TeamService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
  public ResponseEntity<TeamDto> createTeam(@RequestBody TeamDto teamDto) {
    var team = MAPPER.map(teamDto, Team.class);
    team = teamService.save(team);
    var teamDtoResponse = new TeamDto(team);
    return new ResponseEntity<>(teamDtoResponse, HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<TeamDto> updateTeam(@RequestBody TeamDto teamDto) {
    var team = MAPPER.map(teamDto, Team.class);
    team = teamService.save(team);
    var teamDtoResponse = new TeamDto(team);

    return new ResponseEntity<>(teamDtoResponse, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity deleteTeam(@PathVariable Long id) {
    teamService.delete(id);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  private List<Team> getTeamsByStatus(Boolean deleted) {
    return deleted == null ? teamService.findAll() : teamService.findAllByDeleted(deleted);
  }

}
