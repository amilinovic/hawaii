package eu.execom.hawaii.service;

import eu.execom.hawaii.model.Absence;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.repository.AbsenceRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class AbsenceServiceTest {

  @Mock
  private AbsenceRepository absenceRepository;

  @InjectMocks
  private AbsenceService absenceService;

  private User mockUser;
  private Absence mockAbsence;
  private List<Absence> absences;

  @Before
  public void setUp() {
    mockUser = EntityBuilder.user(EntityBuilder.team());

    mockAbsence = EntityBuilder.absenceTraining();

    var mockAbsence2 = EntityBuilder.absenceAnnual();
    var mockAbsence3 = EntityBuilder.absence();

    absences = Arrays.asList(mockAbsence, mockAbsence2, mockAbsence3);
  }

  @Test
  public void shouldGetAllAbsences() {
    // given
    given(absenceRepository.findAll()).willReturn(absences);

    // when
    List<Absence> absences = absenceRepository.findAll();

    // then
    assertNotNull(absences);
    assertThat("Expect size to be three", absences.size(), is(3));
    assertThat("Expect name to be 'Training'", absences.get(0).getName(), is("Training"));
    verify(absenceRepository).findAll();
    verifyNoMoreInteractions(absenceRepository);
  }

  @Test
  public void shouldGetAbsenceById() {
    // given
    var absenceId = 1L;
    given(absenceRepository.getOne(absenceId)).willReturn(mockAbsence);

    // when
    var absence = absenceService.getById(absenceId);

    // then
    assertThat("Expect name to be 'Training'", absence.getName(), is("Training"));
    verify(absenceRepository).getOne(anyLong());
    verifyNoMoreInteractions(absenceRepository);
  }

  @Test
  public void shouldSaveAbsence() {
    // given
    given(absenceRepository.save(mockAbsence)).willReturn(mockAbsence);

    // when
    Absence absence = absenceService.save(mockAbsence);

    // then
    assertNotNull(absence);
    verify(absenceRepository).save(mockAbsence);
    verifyNoMoreInteractions(absenceRepository);
  }

  @Test
  public void shouldDeleteAbsence() {
    // given
    var absenceId = mockAbsence.getId();
    given(absenceRepository.existsById(absenceId)).willReturn(true);

    // when
    absenceService.delete(absenceId);

    // then
    verify(absenceRepository).existsById(anyLong());
    verify(absenceRepository).deleteById(anyLong());
    verifyNoMoreInteractions(absenceRepository);
  }
}