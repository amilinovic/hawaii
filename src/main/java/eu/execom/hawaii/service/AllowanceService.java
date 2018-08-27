package eu.execom.hawaii.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.execom.hawaii.exceptions.InsufficientHoursException;
import eu.execom.hawaii.model.Allowance;
import eu.execom.hawaii.model.Day;
import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.AbsenceSubtype;
import eu.execom.hawaii.model.enumerations.AbsenceType;
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
   * Apply pending request on request user allowance.
   *
   * @param request the Request.
   * @param pendingCanceled indicate should be pending removed.
   */
  @Transactional
  public void applyPendingRequest(Request request, boolean pendingCanceled) {
    var allowance = getByUser(request.getUser());
    var absence = request.getAbsence();
    var days = request.getDays();
    var hours = calculateHours(days);
    if (pendingCanceled) {
      hours = -hours;
    }

    var isAbsenceTypeDeductible = AbsenceType.DEDUCTED_LEAVE.equals(absence.getAbsenceType());
    var isAbsenceTypeAnnual = AbsenceSubtype.ANNUAL.equals(absence.getAbsenceSubtype());
    var isAbsenceTypeTraining = AbsenceSubtype.TRAINING.equals(absence.getAbsenceSubtype());

    if (isAbsenceTypeDeductible && isAbsenceTypeAnnual) {
      checkRemainingAnnualHours(allowance, hours);
      applyPendingAnnual(allowance, hours);
    } else if (isAbsenceTypeDeductible && isAbsenceTypeTraining) {
      checkRemainingTrainingHours(allowance, hours);
      applyPendingTraining(allowance, hours);
    }

  }

  private void applyPendingAnnual(Allowance allowance, int requestedHours) {
    var allowancePendingAnnual = allowance.getPendingAnnual();
    var calculatedPendingAnnual = allowancePendingAnnual + requestedHours;
    allowance.setPendingAnnual(calculatedPendingAnnual);
    allowanceRepository.save(allowance);
  }

  private void applyPendingTraining(Allowance allowance, int requestedHours) {
    var allowancePendingTraining = allowance.getPendingTraining();
    var calculatedPendingAnnual = allowancePendingTraining + requestedHours;
    allowance.setTakenTraining(calculatedPendingAnnual);
    allowanceRepository.save(allowance);
  }

  /**
   * Apply request after approval or cancellation on the request user allowance.
   *
   * @param request the Request.
   */
  @Transactional
  public void applyRequest(Request request, boolean requestCanceled) {
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
            checkRemainingAnnualHours(allowance, hours);
            applyAnnual(allowance, hours);
            break;
          case TRAINING:
            checkRemainingTrainingHours(allowance, hours);
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
    var calculatedAnnual = allowance.getTakenAnnual() + requestedHours;
    allowance.setTakenAnnual(calculatedAnnual);
    allowanceRepository.save(allowance);
  }

  private void applyTraining(Allowance allowance, int requestedHours) {
    var calculatedTraining = allowance.getTakenTraining() + requestedHours;
    allowance.setTakenTraining(calculatedTraining);
    allowanceRepository.save(allowance);
  }

  private void applySickness(Allowance allowance, int hours) {
    var calculatedSickness = allowance.getSickness() + hours;
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

  private void checkRemainingAnnualHours(Allowance allowance, int requestedHours) {
    var userEmail = allowance.getUser().getEmail();
    var remainingHours = calculateRemainingAnnualHours(allowance);
    if (requestedHours > remainingHours) {
      log.error("Insufficient hours: available {}, requested {}, for user with email {}", remainingHours,
          requestedHours, userEmail);
      throw new InsufficientHoursException();
    }
  }

  private int calculateRemainingAnnualHours(Allowance allowance) {
    var totalHours =
        allowance.getAnnual() + allowance.getBonus() + allowance.getCarriedOver() + allowance.getManualAdjust();
    var takenAnnual = allowance.getTakenAnnual();
    var pendingAnnual = allowance.getPendingAnnual();

    return totalHours - takenAnnual - pendingAnnual;
  }

  private void checkRemainingTrainingHours(Allowance allowance, int requestedHours) {
    var userEmail = allowance.getUser().getEmail();
    var remainingHours = calculateRemainingTrainingHours(allowance);
    if (requestedHours > remainingHours) {
      log.error("Insufficient hours: available {}, requested {}, for user with email {}", remainingHours,
          requestedHours, userEmail);
      throw new InsufficientHoursException();
    }
  }

  private int calculateRemainingTrainingHours(Allowance allowance) {
    var totalTraining = allowance.getTraining();
    var takenTraining = allowance.getTakenTraining();
    var pendingTraining = allowance.getPendingTraining();

    return totalTraining - takenTraining - pendingTraining;
  }

}
