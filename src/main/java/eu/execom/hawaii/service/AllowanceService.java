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
  void applyRequest(Request request, boolean apply) {
    var allowance = getByUser(request.getUser());
    var absence = request.getAbsence();
    var days = request.getDays();
    var hours = calculateHours(days);
    if (!apply) {
      hours = -hours;
    }

    switch (absence.getAbsenceType()) {
      case LEAVE:
        if (absence.isDeducted()) {
          applyAnnual(allowance, hours);
        }
        break;
      case SICKNESS:
        applySickness(allowance, hours);
        break;
      case BONUS_DAYS:
        applyBonus(allowance, hours);
        break;
    }
  }

  private void applyAnnual(Allowance allowance, int hours) {
    var calculatedAnnual = allowance.getAnnual() - hours;
    allowance.setAnnual(calculatedAnnual);
    allowanceRepository.save(allowance);
  }

  private void applySickness(Allowance allowance, int hours) {
    int calculatedSickness = allowance.getSickness() + hours;
    allowance.setSickness(calculatedSickness);
    allowanceRepository.save(allowance);
  }

  private void applyBonus(Allowance allowance, int hours) {
    int calculatedBonus = allowance.getBonus() + hours;
    allowance.setBonus(calculatedBonus);
    allowanceRepository.save(allowance);
  }

  private int calculateHours(List<Day> days) {
    return days.stream().mapToInt(this::getHoursFromDay).sum();
  }

  private int getHoursFromDay(Day day) {
    return Duration.FULL_DAY.equals(day.getDuration()) ? FULL_DAY : HALF_DAY;
  }

}
