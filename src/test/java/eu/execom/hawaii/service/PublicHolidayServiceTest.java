package eu.execom.hawaii.service;

import eu.execom.hawaii.model.PublicHoliday;
import eu.execom.hawaii.repository.PublicHolidayRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class PublicHolidayServiceTest {

  @Mock
  private PublicHolidayRepository publicHolidayRepository;

  @InjectMocks
  private PublicHolidayService publicHolidayService;

  private PublicHoliday mockPublicHoliday;
  private List<PublicHoliday> mockPublicHolidays;

  @Before
  public void setUp() {
    mockPublicHoliday = EntityBuilder.publicholiday();
    var mockPublicHoliday2 = EntityBuilder.publicholiday();

    mockPublicHolidays = new ArrayList<>(Arrays.asList(mockPublicHoliday, mockPublicHoliday2));
  }

  @Test
  public void shouldGetPublicHolidayById() {
    // given
    var userId = 1L;
    given(publicHolidayRepository.getOne(userId)).willReturn(mockPublicHoliday);

    // when
    PublicHoliday publicHoliday = publicHolidayService.getById(userId);

    // then
    assertThat("Expect name to be New Year", publicHoliday.getName(), is("New year"));
    verify(publicHolidayRepository).getOne(anyLong());
    verifyNoMoreInteractions(publicHolidayRepository);
  }

  @Test
  public void shouldGetAllPublicHolidays() {
    // given
    var active = true;
    given(publicHolidayRepository.findAllByActive(active)).willReturn(mockPublicHolidays);

    // when
    List<PublicHoliday> publicHolidays = publicHolidayService.findAllByActive(active);

    // then
    assertThat("Expect size to be two", publicHolidays.size(), is(2));
    verify(publicHolidayRepository).findAllByActive(anyBoolean());
    verifyNoMoreInteractions(publicHolidayRepository);
  }

  @Test
  public void shouldSavePublicHoliday() {
    // given
    given(publicHolidayRepository.save(mockPublicHoliday)).willReturn(mockPublicHoliday);

    // when
    PublicHoliday publicHoliday = publicHolidayService.save(mockPublicHoliday);

    // then
    assertThat("Expect name to be New Year", publicHoliday.getName(), is(mockPublicHoliday.getName()));
    verify(publicHolidayRepository).save(ArgumentMatchers.any());
    verifyNoMoreInteractions(publicHolidayRepository);
  }

  @Test
  public void shouldDeletePublicHoliday() {
    // given
    var publicHolidayId = 1L;
    given(publicHolidayRepository.getOne(publicHolidayId)).willReturn(mockPublicHoliday);

    // when
    publicHolidayService.delete(publicHolidayId);

    // then
    verify(publicHolidayRepository).getOne(anyLong());
    verify(publicHolidayRepository).save(ArgumentMatchers.any());
    verifyNoMoreInteractions(publicHolidayRepository);
  }

}