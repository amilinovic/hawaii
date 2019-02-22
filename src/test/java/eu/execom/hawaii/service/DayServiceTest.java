package eu.execom.hawaii.service;

import eu.execom.hawaii.model.Absence;
import eu.execom.hawaii.model.Day;
import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.repository.DayRepository;
import eu.execom.hawaii.repository.RequestRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class DayServiceTest {

  @Mock
  private DayRepository dayRepository;
  @Mock
  private RequestRepository requestRepository;

  @InjectMocks
  private DayService dayService;

  private User mockUser;
  private LocalDate date;
  private LocalDate date2;
  private List<Request> mockRequests = new ArrayList<>();
  private List<Day> mockDays;
  private Object[] allMocks;

  @Before
  public void setUp() {
    Absence absence = EntityBuilder.absence();
    Day day1 = EntityBuilder.day(LocalDate.of(2019, 1, 1));
    Day day2 = EntityBuilder.day(LocalDate.of(2019, 1, 5));
    mockDays = List.of(day1, day2);
    var mockRequest = EntityBuilder.request(absence, List.of(day1));
    mockRequests = List.of(mockRequest);
    mockUser = mockRequest.getUser();
    date = day1.getDate();
    date2 = day2.getDate();

    allMocks = new Object[] {requestRepository, dayRepository};
  }

  @Test
  public void shouldGetUserAbsencesDays() {
    // given
    given(requestRepository.findAllByUser(mockUser)).willReturn(mockRequests);
    given(dayRepository.findAllByRequestInAndDateIsBetween(mockRequests, date, date2)).willReturn(mockDays);

    // when
    List<Day> days = dayService.getUserAbsencesDays(mockUser, date, date2);

    // then
    assertNotNull(mockDays);
    assertThat("Expect list size to be two", days.size(), is(2));
    verify(requestRepository).findAllByUser(any());
    verify(dayRepository).findAllByRequestInAndDateIsBetween(any(), any(), any());
    verifyNoMoreInteractions(allMocks);
  }

}