package eu.execom.hawaii.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import eu.execom.hawaii.repository.AllowanceRepository;

@RunWith(MockitoJUnitRunner.class)
public class AllowanceServiceTest {

  @Mock
  AllowanceRepository allowanceRepository;

  @InjectMocks
  AllowanceService allowanceService;


  @Test
  public void shouldAllowanceByUser() {
    // given

    // when


    // then

  }

  @Test
  public void shouldApplyRequestWithAbsenceTypeDeducted() {

    // given

    // when

    // then

  }

  @Test
  public void shouldApplyRequestWithAbsenceSubtypeAnnual() {

    // given

    // when

    // then

  }

  @Test
  public void shouldApplyRequestWithAbsenceSubtypeTraining() {

    // given

    // when

    // then

  }

  @Test
  public void shouldApplyRequestWithAbsenceTypeSickness() {

    // given

    // when

    // then

  }

  @Test
  public void shouldApplyRequestWithAbsenceTypeBonusDays() {

    // given

    // when

    // then

  }

  @Test
  public void shouldFailApplyRequestDueWrongAbsenceSubtype() {

    // given

    // when

    // then

  }

}