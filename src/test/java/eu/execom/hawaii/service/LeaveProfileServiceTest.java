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
  private LeaveProfileRepository leaveProfileRepo;
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
    given(leaveProfileRepo.findAll()).willReturn(leaveProfiles);

    //when
    List<LeaveProfile> leaveProfiles = leaveService.findAll();

    //then
    assertNotNull(leaveProfiles);
    assertThat("Expect size to be two", leaveProfiles.size(), is(2));
    assertThat("Expect name to be 'Default'", leaveProfiles.get(0).getName(), is("Default"));
    verify(leaveProfileRepo).findAll();
    verifyNoMoreInteractions(leaveProfileRepo);
  }

  @Test
  public void shouldGetLeaveProfileById() {
    //given
    var leaveProfileId = 1L;
    given(leaveProfileRepo.getOne(leaveProfileId)).willReturn(mockLeaveProfile);

    //when
    var leaveProfile = leaveService.getById(leaveProfileId);

    //then
    assertThat("Expect name to be 'Default'", leaveProfile.getName(), is("Default"));
    verify(leaveProfileRepo).getOne(anyLong());
    verifyNoMoreInteractions(leaveProfileRepo);
  }

  @Test
  public void shouldCreateLeaveProfile() {
    //given
    var leaveProfile3 = EntityBuilder.leaveProfileIII();
    given(leaveProfileRepo.existsByLeaveProfileType(mockLeaveProfile.getLeaveProfileType())).willReturn(false);
    given(leaveProfileRepo.save(mockLeaveProfile)).willReturn(leaveProfile3);

    //when
    var leaveProfile = leaveService.create(mockLeaveProfile);

    //then
    assertNotNull(leaveProfile);
    assertThat("Expect name to be 'Ten-Fifteen'", leaveProfile.getName(), is("Ten-Fifteen"));
    verify(leaveProfileRepo).existsByLeaveProfileType(any());
    verify(leaveProfileRepo).save(any());
    verifyNoMoreInteractions(leaveProfileRepo);
  }

  @Test
  public void shouldSaveLeaveProfile() {
    //given
    given(leaveProfileRepo.save(mockLeaveProfile)).willReturn(mockLeaveProfile);

    //when
    LeaveProfile leaveProfile = leaveProfileRepo.save(mockLeaveProfile);

    //then
    assertNotNull(leaveProfile);
    verify(leaveProfileRepo).save(mockLeaveProfile);
    verifyNoMoreInteractions(leaveProfileRepo);
  }

}