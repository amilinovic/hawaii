package eu.execom.hawaii.service;

import eu.execom.hawaii.model.Day;
import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.repository.DayRepository;
import eu.execom.hawaii.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Absence type management service.
 */
@Service
public class DayService {

  private DayRepository dayRepository;
  private RequestRepository requestRepository;

  @Autowired
  public DayService(DayRepository dayRepository, RequestRepository requestRepository) {
    this.dayRepository = dayRepository;
    this.requestRepository = requestRepository;
  }

  /**
   * Get Days of absence of user between two dates
   *
   * @param user      - selected user.
   * @param startDate - Starting with date
   * @param endDate   - Ending with date
   * @return found days
   */
  public List<Day> getUserAbsencesDays(User user, LocalDate startDate, LocalDate endDate) {

    List<Request> requests = this.requestRepository.findAllByUser(user);

    return dayRepository.findAllByRequestInAndDateIsBetween(requests, startDate, endDate);
  }

}
