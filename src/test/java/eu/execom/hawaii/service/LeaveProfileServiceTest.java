package eu.execom.hawaii.service;

import eu.execom.hawaii.model.LeaveProfile;
import eu.execom.hawaii.repository.LeaveProfileRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class LeaveProfileServiceTest {

  @Mock
  private LeaveProfileRepository leaveProfileRepository;
  @InjectMocks
  private LeaveProfileService leaveService;
  private LeaveProfile mockLeaveProfile;
  private LeaveProfile mockLeaveProfile2;

  private List<LeaveProfile> leaveProfiles;

  @Before
  public void setUp() {
    mockLeaveProfile = EntityBuilder.leaveProfile();
    mockLeaveProfile2 = EntityBuilder.leaveProfileII();

    leaveProfiles = List.of(mockLeaveProfile, mockLeaveProfile2);
  }

  @Test
  public void shouldGetAllLeaveProfiles() {
    //given
    given(leaveProfileRepository.findAll()).willReturn(leaveProfiles);

    //when
    List<LeaveProfile> leaveProfiles = leaveService.findAll();

    //then
    assertNotNull(leaveProfiles);
    assertThat("Expect size to be two", leaveProfiles.size(), is(2));
    assertThat("Expect name to be 'Default'", leaveProfiles.get(0).getName(), is("Default"));
    verify(leaveProfileRepository).findAll();
    verifyNoMoreInteractions(leaveProfileRepository);
  }

  @Test
  public void shouldGetLeaveProfileById() {
    //given
    var leaveProfileId = 1L;
    given(leaveProfileRepository.getOne(leaveProfileId)).willReturn(mockLeaveProfile);

    //when
    var leaveProfile = leaveService.getById(leaveProfileId);

    //then
    assertThat("Expect name to be 'Default'", leaveProfile.getName(), is("Default"));
    verify(leaveProfileRepository).getOne(anyLong());
    verifyNoMoreInteractions(leaveProfileRepository);
  }

  @Test
  public void shouldCreateLeaveProfile() {
    //given
    var leaveProfile3 = EntityBuilder.leaveProfileIII();
    given(leaveProfileRepository.save(mockLeaveProfile)).willReturn(leaveProfile3);

    //when
    var leaveProfile = leaveService.create(mockLeaveProfile);

    //then
    assertNotNull(leaveProfile);
    assertThat("Expect name to be 'Ten-Fifteen'", leaveProfile.getName(), is("Ten-Fifteen"));
    verify(leaveProfileRepository).save(any());
    verifyNoMoreInteractions(leaveProfileRepository);
  }

  @Test
  public void shouldSaveLeaveProfile() {
    //given
    given(leaveProfileRepository.save(mockLeaveProfile)).willReturn(mockLeaveProfile);

    //when
    LeaveProfile leaveProfile = leaveProfileRepository.save(mockLeaveProfile);

    //then
    assertNotNull(leaveProfile);
    verify(leaveProfileRepository).save(mockLeaveProfile);
    verifyNoMoreInteractions(leaveProfileRepository);
  }

}