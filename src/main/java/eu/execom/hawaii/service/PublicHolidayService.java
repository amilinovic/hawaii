package eu.execom.hawaii.service;

import eu.execom.hawaii.model.PublicHoliday;
import eu.execom.hawaii.repository.PublicHolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Public holiday management service.
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
   */
  public PublicHoliday getById(Long id) {
    return publicHolidayRepository.getOne(id);
  }

  /**
   * Retrieves all publicHolidays.
   *
   * @param deleted is it deleted.
   * @return a list of publicHolidays.
   */
  public List<PublicHoliday> findAllByDeleted(boolean deleted) {
    return publicHolidayRepository.findAllByDeleted(deleted);
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
    publicHoliday.setDeleted(true);
    publicHolidayRepository.save(publicHoliday);
  }

}
