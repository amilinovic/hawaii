package eu.execom.hawaii.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import eu.execom.hawaii.model.Team;
import eu.execom.hawaii.repository.TeamRepository;

@RunWith(MockitoJUnitRunner.class)
public class TeamServiceTest {

  @Mock
  private TeamRepository teamRepository;

  @InjectMocks
  private TeamService teamService;

  private Team mockTeam;
  private List<Team> mockteams;

  @Before
  public void setUp() {
   mockTeam = EntityBuilder.team();
   var mockTeam2 = EntityBuilder.team();
   mockTeam2.setName("My team 2");

   mockteams = new ArrayList<>(Arrays.asList(mockTeam, mockTeam2));
  }

  @Test
  public void shouldGetAllTeams() {
    // given
    var acitve = true;
    given(teamRepository.findAllByActive(acitve)).willReturn(mockteams);

    // when
    List<Team> teams = teamService.findAll(acitve);

    // then
    assertThat("Expect size to be two", teams.size(), is(2));
    assertThat("Expect name to be My team1", teams.get(0).getName(), is("My team1"));
    verify(teamRepository).findAll();
    verifyNoMoreInteractions(teamRepository);
  }

  @Test
  public void shouldGetTeamById() {
    // given
    var teamId = 1L;
    given(teamRepository.findById(teamId)).willReturn(Optional.of(mockTeam));

    // when
    var team = teamService.getById(teamId);

    // then
    assertThat("Expect name to be My team1", team.getName(), is("My team1"));
    verify(teamRepository).findById(anyLong());
    verifyNoMoreInteractions(teamRepository);
  }

  @Test
  public void shouldSaveTeam() {
    // given

    given(teamRepository.save(mockTeam)).willReturn(mockTeam);

    // when
    var team = teamService.save(mockTeam);

    // then
    assertNotNull(team);
    verify(teamRepository).save(mockTeam);
    verifyNoMoreInteractions(teamRepository);
  }

  @Test
  public void shouldDeleteTeam() {
    // given
    var teamId = 1L;
    given(teamRepository.findById(teamId)).willReturn(Optional.of(mockTeam));

    // when
    teamService.delete(teamId);

    // then
    verify(teamRepository).findById(anyLong());
    verify(teamRepository).save(any());
    verifyNoMoreInteractions(teamRepository);
  }

}