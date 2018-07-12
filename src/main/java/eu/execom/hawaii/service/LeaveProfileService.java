package eu.execom.hawaii.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.execom.hawaii.model.LeaveProfile;
import eu.execom.hawaii.repository.LeaveProfileRepository;

/**
 * Leave profile management service.
 */
@Service
public class LeaveProfileService {

  private LeaveProfileRepository leaveProfileRepository;

  @Autowired
  public LeaveProfileService(LeaveProfileRepository leaveProfileRepository) {
    this.leaveProfileRepository = leaveProfileRepository;
  }

  /**
   * Retrieves a list of all leave profiles from repository.
   *
   * @return a list of all leave profiles.
   */
  public List<LeaveProfile> getAll() {
    return leaveProfileRepository.findAll();
  }

  /**
   * Retrieves a leave profile with a specific id.
   *
   * @param id Leave profile id.
   * @return Leave profile with provided id if exists.
   * @throws EntityNotFoundException if a leave profile with given id is not found.
   */
  public LeaveProfile getById(Long id) {
    return leaveProfileRepository.getOne(id);
  }

  /**
   * Saves the provided leave profile to repository.
   *
   * @param leaveProfile the Team entity to be persisted.
   */
  public LeaveProfile save(LeaveProfile leaveProfile) {
    return leaveProfileRepository.save(leaveProfile);
  }

  /**
   * Deletes leave profile.
   *
   * @param id - the leave profile id.
   * @throws EntityNotFoundException if a leave profile with given id is not found.
   */
  public void delete(Long id) {
    if (!leaveProfileRepository.existsById(id)) {
      throw new EntityNotFoundException();
    }
    leaveProfileRepository.deleteById(id);
  }

}
