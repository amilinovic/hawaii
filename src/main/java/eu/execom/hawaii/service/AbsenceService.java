package eu.execom.hawaii.service;

import eu.execom.hawaii.model.Absence;
import eu.execom.hawaii.repository.AbsenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * Absence type management service.
 */
@Service
public class AbsenceService {

  private AbsenceRepository absenceRepository;

  @Autowired
  public AbsenceService(AbsenceRepository absenceRepository) {
    this.absenceRepository = absenceRepository;
  }

  /**
   * Retrieves a list of all Absence types from repository.
   *
   * @return a list of all Absence types.
   */
  public List<Absence> findAll() {
    return absenceRepository.findAll();
  }

  /**
   * Retrieves an Absence with a specific id.
   *
   * @param id Absence id.
   * @return Absence with provided id if exists.
   * @throws EntityNotFoundException if an Absence with given id is not found.
   */
  public Absence getById(Long id) {
    return absenceRepository.getOne(id);
  }

  /**
   * Saves the provided Absence to repository.
   *
   * @param absence the Absence entity to be persisted.
   */
  public Absence save(Absence absence) {
    return absenceRepository.save(absence);
  }

  /**
   * Deletes Absence.
   *
   * @param id - the Absence id.
   * @throws EntityNotFoundException if an Absence with given id is not found.
   */
  public void delete(Long id) {
    if (!absenceRepository.existsById(id)) {
      throw new EntityNotFoundException("Absence already deleted.");
    }
    absenceRepository.deleteById(id);
  }

}
