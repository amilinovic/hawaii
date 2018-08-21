package eu.execom.hawaii.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.execom.hawaii.exceptions.InsufficientHoursException;
import eu.execom.hawaii.model.Allowance;
import eu.execom.hawaii.model.Day;
import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.Duration;
import eu.execom.hawaii.repository.AllowanceRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    var absenceType = absence.getAbsenceType();
    var absenceSubtype = absence.getAbsenceSubtype();
    switch (absenceType) {
      case DEDUCTED_LEAVE:
        if (absenceSubtype == null) {
          throw new IllegalArgumentException("Absence subtype cannot be null.");
        }
        switch (absenceSubtype) {
          case ANNUAL:
            applyAnnual(allowance, hours);
            break;
          case TRAINING:
            applyTraining(allowance, hours);
            break;
          default:
            throw new IllegalArgumentException("Unsupported absence subtype: " + absenceSubtype);
        }
        break;
      case NON_DEDUCTED_LEAVE:
        break;
      case SICKNESS:
        applySickness(allowance, hours);
        break;
      case BONUS_DAYS:
        applyBonus(allowance, hours);
        break;
      default:
        throw new IllegalArgumentException("Unsupported absence type: " + absenceType);
    }
  }

  private void applyAnnual(Allowance allowance, int requestedHours) {
    var userEmail = allowance.getUser().getEmail();
    int remainingHours = calculateRemainingAnnualHours(allowance);
    if (requestedHours > remainingHours) {
      log.error("Insufficient hours: available {}, requested {}, for user with email {}", remainingHours, requestedHours,
          userEmail);
      throw new InsufficientHoursException();
    }
    var calculatedAnnual = allowance.getTakenAnnual() + requestedHours;
    allowance.setTakenAnnual(calculatedAnnual);
    allowanceRepository.save(allowance);
  }

  private void applyTraining(Allowance allowance, int requestedHours) {
    var userEmail = allowance.getUser().getEmail();
    int remainingHours = calculateRemainingTrainingHours(allowance);
    if (requestedHours > remainingHours) {
      log.error("Insufficient hours: available {}, requested {}, for user with email {}", remainingHours, requestedHours,
          userEmail);
      throw new InsufficientHoursException();
    }
    var calculatedTraining = allowance.getTakenTraining() + requestedHours;
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

  private int calculateRemainingAnnualHours(Allowance allowance) {
    int totalHours =
        allowance.getAnnual() + allowance.getBonus() + allowance.getCarriedOver() + allowance.getManualAdjust();
    int takenAnnual = allowance.getTakenAnnual();

    return totalHours - takenAnnual;
  }

  private int calculateRemainingTrainingHours(Allowance allowance) {
    return allowance.getTraining() - allowance.getTakenTraining();
  }

}
