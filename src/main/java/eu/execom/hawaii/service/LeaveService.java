package eu.execom.hawaii.service;

import eu.execom.hawaii.model.absence.Leave;
import eu.execom.hawaii.repository.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * Leave type management service.
 */
@Service
public class LeaveService {

  private LeaveRepository leaveRepository;

  @Autowired
  public LeaveService(LeaveRepository leaveRepository) {
    this.leaveRepository = leaveRepository;
  }

  /**
   * Retrieves a list of all leave types from repository.
   *
   * @return a list of all leave types
   */
  public List<Leave> findAll() {
    return leaveRepository.findAll();
  }

  /**
   * Retrieves a leave with a specific id.
   *
   * @param id Leave id
   * @return Leave with provided id if exists
   * @throws EntityNotFoundException if a leave with given id is not found
   */
  public Leave getById(Long id) {
    return leaveRepository.getOne(id);
  }

  /**
   * Saves the provided leave to repository.
   *
   * @param leave the Leave entity to be persisted.
   */
  public Leave save(Leave leave) {
    return leaveRepository.save(leave);
  }

  /**
   * Deletes leave.
   *
   * @param id - the leave id
   * @throws EntityNotFoundException if a leave with given id is not found
   */
  public void delete(Long id) {
    if (!leaveRepository.existsById(id)) {
      throw new EntityNotFoundException();
    }
    leaveRepository.deleteById(id);
  }
}
