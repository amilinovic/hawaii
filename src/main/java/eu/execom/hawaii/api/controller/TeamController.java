package eu.execom.hawaii.api.controller;

import eu.execom.hawaii.dto.TeamDto;
import eu.execom.hawaii.model.Team;
import eu.execom.hawaii.service.TeamService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/teams")
public class TeamController {

  private TeamService teamService;

  @Autowired
  public TeamController(TeamService teamService) {
    this.teamService = teamService;
  }

  private static final ModelMapper MAPPER = new ModelMapper();

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<TeamDto>> getTeams() {
    List<Team> teams = teamService.getAll();
    List<TeamDto> teamDtos = teams.stream().map(TeamDto::new).collect(Collectors.toList());
    return new ResponseEntity<>(teamDtos, HttpStatus.OK);
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TeamDto> getTeam(@PathVariable Long id) {
    Team team = teamService.getById(id);
    TeamDto teamDto = new TeamDto(team);
    return new ResponseEntity<>(teamDto, HttpStatus.OK);
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TeamDto> createTeam(@RequestBody TeamDto teamDto) {
    Team team = MAPPER.map(teamDto, Team.class);
    team = teamService.save(team);
    TeamDto teamDtoResponse = new TeamDto(team);
    return new ResponseEntity<>(teamDtoResponse, HttpStatus.CREATED);
  }

  @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TeamDto> updateTeam(@RequestBody TeamDto teamDto) {
    Team team = MAPPER.map(teamDto, Team.class);
    team = teamService.save(team);
    TeamDto teamDtoResponse = new TeamDto(team);
    return new ResponseEntity<>(teamDtoResponse, HttpStatus.OK);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity deleteTeam(@PathVariable Long id) {
    teamService.delete(id);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

}
