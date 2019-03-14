package eu.execom.hawaii.service;

import eu.execom.hawaii.model.Year;
import eu.execom.hawaii.repository.AllowanceRepository;
import eu.execom.hawaii.repository.UserRepository;
import eu.execom.hawaii.repository.YearRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class YearServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private YearRepository yearRepository;

  @Mock
  private AllowanceRepository allowanceRepository;

  @InjectMocks
  private YearService yearService;

  private Year mockYear;
  private List<Year> mockAllYears;

  @Before
  public void setUp() {
    mockYear = EntityBuilder.thisYear();
    var mockNextYear = EntityBuilder.nextYear();

    mockAllYears = Arrays.asList(mockYear, mockNextYear);
  }

  @Test
  public void shouldGetYearsById() {
    // given
    var yearId = 1L;
    given(yearRepository.getOne(yearId)).willReturn(mockYear);

    // when
    Year year = yearService.getById(yearId);

    // then
    assertThat("Expected year is current year", year.getYear(), is(EntityBuilder.thisYear().getYear()));
    verify(yearRepository).getOne(anyLong());
    verifyNoMoreInteractions(yearRepository);
  }

  @Test
  public void shouldGetAllYears() {
    // given
    given(yearRepository.findAll()).willReturn(mockAllYears);

    // when
    List<Year> allYears = yearRepository.findAll();

    // then
    assertThat("Expect size to be two", allYears.size(), is(2));
    verify(yearRepository).findAll();
    verifyNoMoreInteractions(yearRepository);
  }

  @Test
  public void shouldSaveYear() {
    // given
    given(yearRepository.save(mockYear)).willReturn(mockYear);

    // when
    Year year = yearService.save(mockYear);

    // then
    assertThat("Expected year is current year", year.getYear(), is(EntityBuilder.thisYear().getYear()));
    verify(yearRepository).save(ArgumentMatchers.any());
    verifyNoMoreInteractions(yearRepository);
  }

  @Test
  public void shouldDeleteYear() {
    // given
    var yearId = 1L;
    mockYear.setActive(false);
    given(yearRepository.getOne(yearId)).willReturn(mockYear);

    // when
    yearService.delete(yearId);

    // then
    verify(yearRepository).getOne(anyLong());
    verify(yearRepository).deleteById(anyLong());
    verifyNoMoreInteractions(yearRepository);
  }

  @Test
  public void shouldCreateAllowanceOnCreateYear() {
    // given
    var user1 = EntityBuilder.user(EntityBuilder.team());
    var user2 = EntityBuilder.approver();
    var activeUsers = List.of(user1, user2);

    given(userRepository.findAllByUserStatusTypeIn(any())).willReturn(activeUsers);
    given(allowanceRepository.save(any())).willReturn(any());

    // when
    yearService.createAllowanceOnCreateYear(EntityBuilder.nextYear());

    // then
    assertThat("Expect to have one allowance created for user 1", user1.getAllowances().size(), is(1));
    assertThat("Expect to have one allowance created for user 2", user2.getAllowances().size(), is(1));
    assertThat("Expect allowance to be for next year", user1.getAllowances().get(0).getYear().getYear(),
        is(EntityBuilder.nextYear().getYear()));
    assertThat("Expect allowance to be for next year", user2.getAllowances().get(0).getYear().getYear(),
        is(EntityBuilder.nextYear().getYear()));
    verify(userRepository).findAllByUserStatusTypeIn(any());
    verify(allowanceRepository, times(2)).save(any());
    verifyNoMoreInteractions(yearRepository, allowanceRepository);
  }
}

