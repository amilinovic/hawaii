package eu.execom.hawaii.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.execom.hawaii.model.PublicHoliday;
import eu.execom.hawaii.repository.PublicHolidayRepository;

/**
 * Public holiday management service.
 *
 */
@Service
public class PublicHolidayService {

  private PublicHolidayRepository publicHolidayRepository;

  @Autowired
  public PublicHolidayService(PublicHolidayRepository publicHolidayRepository) {
    this.publicHolidayRepository = publicHolidayRepository;
  }

  /**
   * Retrieves a publicHoliday with a specific id.
   *
   * @param id the PublicHoliday id.
   * @return the PublicHoliday.
   * @throws EntityNotFoundException if a publicHoliday with given id is not found.
   */
  public PublicHoliday getById(Long id) {
    return publicHolidayRepository.findById(id).orElseThrow(EntityNotFoundException::new);
  }

  /**
   * Retrieves all publicHolidays.
   *
   * @return a list of publicHolidays.
   */
  public List<PublicHoliday> getAll() {
    return publicHolidayRepository.findAll();
  }

  /**
   * Saves/update the provided PublicHoliday.
   *
   * @param publicHoliday the PublicHoliday to be persisted.
   * @return the PublicHoliday.
   */
  public PublicHoliday save(PublicHoliday publicHoliday) {
    return publicHolidayRepository.save(publicHoliday);
  }

  /**
   * Logically deletes PublicHoliday.
   *
   * @param id the PublicHoliday id.
   */
  public void delete(Long id) {
    var publicHoliday = getById(id);
    publicHoliday.setActive(false);
    publicHolidayRepository.save(publicHoliday);
  }

}
