package eu.execom.hawaii.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.execom.hawaii.model.Allowance;
import eu.execom.hawaii.model.Day;
import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.Duration;
import eu.execom.hawaii.repository.AllowanceRepository;

@Service
public class AllowanceService {

  private static final int HALF_DAY = 4;
  private static final int FULL_DAY = 8;

  private AllowanceRepository allowanceRepository;

  @Autowired
  public AllowanceService(AllowanceRepository allowanceRepository) {
    this.allowanceRepository = allowanceRepository;
  }

  /**
   * Retrieves a Allowance by given user.
   *
   * @param user Allowance user.
   * @return Allowance.
   */
  Allowance getByUser(User user) {
    return allowanceRepository.findByUser(user);
  }

  /**
   * Apply request after approval on request user allowance.
   *
   * @param request the Request.
   */
  void applyRequest(Request request) {
    var allowance = getByUser(request.getUser());
    var absence = request.getAbsence();
    var days = request.getDays();

    switch (absence.getAbsenceType()) {
      case DEDUCTED_LEAVE:
        deductAnnual(allowance, days);
        break;
      case SICKNESS:
        incrementSickness(allowance, days);
        break;
      case BONUS_DAYS:
        incrementBonus(allowance, days);
        break;
      default:
        break;
    }
  }

  private void deductAnnual(Allowance allowance, List<Day> days) {
    allowance.setAnnual(allowance.getAnnual() - calculateHours(days));
    allowanceRepository.save(allowance);
  }

  private void incrementSickness(Allowance allowance, List<Day> days) {
    allowance.setSickness(allowance.getSickness() + calculateHours(days));
    allowanceRepository.save(allowance);
  }

  private void incrementBonus(Allowance allowance, List<Day> days) {
    allowance.setBonus(allowance.getBonus() + calculateHours(days));
    allowanceRepository.save(allowance);
  }

  private int calculateHours(List<Day> days) {
    return days.stream().mapToInt(this::getHoursFromDay).sum();
  }

  private int getHoursFromDay(Day day) {
    return Duration.FULL_DAY.equals(day.getDuration()) ? FULL_DAY : HALF_DAY;
  }

}
