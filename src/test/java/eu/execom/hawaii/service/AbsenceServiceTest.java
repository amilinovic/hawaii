package eu.execom.hawaii.service;

import static org.hamcrest.Matchers.is;
import eu.execom.hawaii.model.Absence;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.repository.AbsenceRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class AbsenceServiceTest {

  @Mock
  private AbsenceRepository absenceRepository;

  @InjectMocks
  private AbsenceService absenceService;

  private User mockUser;
  private Absence mockAbsence;

  @Before
  public void setUp() {
    mockUser = EntityBuilder.user(EntityBuilder.team());

    mockAbsence = EntityBuilder.absenceTraining();
  }

  @Test
  public void shouldGetAbsenceById() {
    //given
    var absenceId = 3L;
    given(absenceRepository.getOne(absenceId)).willReturn(mockAbsence);

    //when
    var absence = absenceService.getById(absenceId);

    //then
    assertThat("Expect name to be Training", absence.getName(), is("Training"));
    verify(absenceRepository).getOne(anyLong());
    verifyNoMoreInteractions(absenceRepository);
  }
}
