package eu.execom.hawaii.service;

import eu.execom.hawaii.model.Team;
import eu.execom.hawaii.repository.TeamRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class TeamServiceTest {

  @Mock
  private TeamRepository teamRepository;

  @Mock
  private AuditInformationService auditInformationService;

  @InjectMocks
  private TeamService teamService;

  private Team mockTeam;
  private List<Team> mockTeams;
  private Object[] allMocks;

  @Before
  public void setUp() {
    mockTeam = EntityBuilder.team();
    var mockTeam2 = EntityBuilder.team();
    mockTeam2.setName("My team 2");

    mockTeams = new ArrayList<>(Arrays.asList(mockTeam, mockTeam2));

    allMocks = new Object[] {teamRepository, auditInformationService};
  }

  @Test
  public void shouldGetAllTeams() {
    //given
    given(teamRepository.findAll()).willReturn(mockTeams);

    //when
    var teams = teamService.findAll();

    //then
    assertThat("Expect name of first element in list to be My team1", teams.get(0).getName(), is("My team1"));
    verify(teamRepository).findAll();
    verifyNoMoreInteractions(allMocks);
  }

  @Test
  public void shouldGetTeamById() {
    // given
    var teamId = 1L;
    given(teamRepository.getOne(teamId)).willReturn(mockTeam);

    // when
    var team = teamService.getById(teamId);

    // then
    assertThat("Expect name to be 'My team1'", team.getName(), is("My team1"));
    verify(teamRepository).getOne(anyLong());
    verifyNoMoreInteractions(allMocks);
  }

  @Test
  public void shouldSaveTeam() {
    // given
    var user = EntityBuilder.approver();
    given(teamRepository.save(mockTeam)).willReturn(mockTeam);

    // when
    var team = teamService.save(mockTeam, user);

    // then
    assertNotNull(team);
    verify(auditInformationService).saveAudit(any(), any(), any(), any(), any());
    verify(teamRepository).save(mockTeam);
    verifyNoMoreInteractions(allMocks);
  }

  @Test
  public void shouldDeleteTeam() {
    // given
    var teamId = 1L;
    given(teamRepository.getOne(teamId)).willReturn(mockTeam);

    // when
    teamService.delete(teamId, any());

    // then
    verify(teamRepository).getOne(anyLong());
    verify(auditInformationService).saveAudit(any(), any(), any(), any(), any());
    verify(teamRepository).deleteById(any());
    verifyNoMoreInteractions(allMocks);
  }

}