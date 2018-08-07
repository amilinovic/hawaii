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
  void applyRequest(Request request, boolean requestCanceled) {
    var allowance = getByUser(request.getUser());
    var absence = request.getAbsence();
    var days = request.getDays();
    var hours = calculateHours(days);
    if (requestCanceled) {
      hours = -hours;
    }

    switch (absence.getAbsenceType()) {
      case DEDUCTED_LEAVE:
        switch (absence.getAbsenceSubtype()) {
          case ANNUAL:
            applyAnnual(allowance, hours);
            break;
          case TRAINING:
            applyTraining(allowance, hours);
            break;
          default:
            break;
        }
        break;
      case SICKNESS:
        applySickness(allowance, hours);
        break;
      case BONUS_DAYS:
        applyBonus(allowance, hours);
        break;
      default:
        break;
    }
  }

  private void applyAnnual(Allowance allowance, int hours) {
    var calculatedAnnual = allowance.getTakenAnnual() + hours;
    allowance.setTakenAnnual(calculatedAnnual);
    allowanceRepository.save(allowance);
  }

  private void applyTraining(Allowance allowance, int hours) {
    var calculatedTraining = allowance.getTakenTraining() + hours;
    allowance.setTakenTraining(calculatedTraining);
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
