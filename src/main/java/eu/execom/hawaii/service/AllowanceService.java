package eu.execom.hawaii.service;

import eu.execom.hawaii.exceptions.InsufficientHoursException;
import eu.execom.hawaii.model.Allowance;
import eu.execom.hawaii.model.Day;
import eu.execom.hawaii.model.LeaveProfile;
import eu.execom.hawaii.model.PublicHoliday;
import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.enumerations.AbsenceSubtype;
import eu.execom.hawaii.model.enumerations.AbsenceType;
import eu.execom.hawaii.model.enumerations.Duration;
import eu.execom.hawaii.repository.AllowanceRepository;
import eu.execom.hawaii.repository.PublicHolidayRepository;
import eu.execom.hawaii.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AllowanceService {

  private static final int HALF_DAY = 4;
  private static final int FULL_DAY = 8;
  private static final int FIVE_DAYS = 40;

  private AllowanceRepository allowanceRepository;
  private PublicHolidayRepository publicHolidayRepository;
  private UserRepository userRepository;

  @Autowired
  public AllowanceService(AllowanceRepository allowanceRepository, PublicHolidayRepository publicHolidayRepository,
      UserRepository userRepository) {
    this.allowanceRepository = allowanceRepository;
    this.publicHolidayRepository = publicHolidayRepository;
    this.userRepository = userRepository;
  }

  /**
   * Apply pending request on request user allowance.
   *
   * @param request the Request.
   */
  @Transactional
  public void applyPendingRequest(Request request, boolean requestCanceled) {
    var yearOfRequest = request.getDays().get(0).getDate().getYear();
    var currentYearAllowance = getByUserAndYear(request.getUser().getId(), yearOfRequest);
    var nextYearAllowance = getByUserAndYear(request.getUser().getId(), yearOfRequest + 1);

    var absence = request.getAbsence();
    var workingDays = getWorkingDaysOnly(request.getDays());
    var hours = calculateHours(workingDays);
    if (requestCanceled) {
      hours = -hours;
    }

    var isAbsenceTypeDeductible = AbsenceType.DEDUCTED_LEAVE.equals(absence.getAbsenceType());
    var isAbsenceTypeAnnual = AbsenceSubtype.ANNUAL.equals(absence.getAbsenceSubtype());
    var isAbsenceTypeTraining = AbsenceSubtype.TRAINING.equals(absence.getAbsenceSubtype());

    if (isAbsenceTypeDeductible && isAbsenceTypeAnnual) {
      checkRemainingAnnualHours(currentYearAllowance, nextYearAllowance, hours);
      if (requestCanceled) {
        cancelPendingAnnual(currentYearAllowance, nextYearAllowance, hours);
      } else {
        applyPendingAnnual(currentYearAllowance, nextYearAllowance, hours);
      }
    } else if (isAbsenceTypeDeductible && isAbsenceTypeTraining) {
      checkRemainingTrainingHours(currentYearAllowance, hours);
      applyPendingTraining(currentYearAllowance, hours);
    }

  }

  /**
   * Retrieves a Allowance by given user.
   *
   * @param userId Allowance user.
   * @return Allowance.
   */
  Allowance getByUserAndYear(Long userId, int year) {
    return allowanceRepository.findByUserIdAndYear(userId, year);
  }

  private void cancelPendingAnnual(Allowance currentYearAllowance, Allowance nextYearAllowance, int requestedHours) {
    var currentYearPendingAnnual = currentYearAllowance.getPendingAnnual();
    var nextYearPendingAnnual = nextYearAllowance.getPendingAnnual();
    var nextYearRequestedHours = nextYearPendingAnnual + requestedHours;

    if (nextYearRequestedHours < 0) {
      currentYearAllowance.setPendingAnnual(currentYearPendingAnnual + nextYearRequestedHours);
      nextYearAllowance.setPendingAnnual(0);

      allowanceRepository.save(currentYearAllowance);
    } else {
      nextYearAllowance.setPendingAnnual(nextYearRequestedHours);
    }

    allowanceRepository.save(nextYearAllowance);
  }

  private void applyPendingAnnual(Allowance currentYearAllowance, Allowance nextYearAllowance, int requestedHours) {
    var currentYearPendingAnnual = currentYearAllowance.getPendingAnnual();
    var nextYearPendingAnnual = nextYearAllowance.getPendingAnnual();

    var remainingHoursCurrentYear = calculateRemainingAnnualHours(currentYearAllowance);
    var nextYearRequestedHours = requestedHours - remainingHoursCurrentYear + nextYearPendingAnnual;

    if (nextYearRequestedHours > 0) {
      currentYearAllowance.setPendingAnnual(currentYearPendingAnnual + remainingHoursCurrentYear);
      nextYearAllowance.setPendingAnnual(nextYearRequestedHours);

      allowanceRepository.save(nextYearAllowance);
    } else {
      currentYearAllowance.setPendingAnnual(currentYearPendingAnnual + requestedHours);
    }

    allowanceRepository.save(currentYearAllowance);
  }

  private void applyPendingTraining(Allowance allowance, int requestedHours) {
    var allowancePendingTraining = allowance.getPendingTraining();
    var calculatedPendingAnnual = allowancePendingTraining + requestedHours;
    allowance.setPendingTraining(calculatedPendingAnnual);
    allowanceRepository.save(allowance);
  }

  /**
   * Apply request after approval or cancellation on the request user allowance.
   *
   * @param request the Request.
   */
  @Transactional
  public void applyRequest(Request request, boolean requestCanceled) {
    var yearOfRequest = request.getDays().get(0).getDate().getYear();
    var currentYearAllowance = getByUserAndYear(request.getUser().getId(), yearOfRequest);
    var nextYearAllowance = getByUserAndYear(request.getUser().getId(), yearOfRequest + 1);
    var absence = request.getAbsence();
    var workingDays = getWorkingDaysOnly(request.getDays());
    var hours = calculateHours(workingDays);
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
            checkRemainingAnnualHours(currentYearAllowance, nextYearAllowance, hours);
            if (requestCanceled) {
              cancelAnnual(currentYearAllowance, nextYearAllowance, hours);
            } else {
              applyAnnual(currentYearAllowance, nextYearAllowance, hours);
            }
            break;
          case TRAINING:
            checkRemainingTrainingHours(currentYearAllowance, hours);
            applyTraining(currentYearAllowance, hours);
            break;
          default:
            throw new IllegalArgumentException("Unsupported absence subtype: " + absenceSubtype);
        }
        break;
      case NON_DEDUCTED_LEAVE:
        break;
      case SICKNESS:
        applySickness(currentYearAllowance, hours);
        break;
      case BONUS_DAYS:
        var leaveProfile = request.getUser().getLeaveProfile();
        checkRemainingBonusHours(leaveProfile, currentYearAllowance, hours);
        applyBonus(currentYearAllowance, hours);
        break;
      default:
        throw new IllegalArgumentException("Unsupported absence type: " + absenceType);
    }
  }

  private void cancelAnnual(Allowance currentYearAllowance, Allowance nextYearAllowance, int requestedHours) {
    var currentYearTakenAnnual = currentYearAllowance.getTakenAnnual();
    var nextYearTakenAnnual = nextYearAllowance.getTakenAnnual();

    var nextYearRequestedHours = nextYearTakenAnnual + requestedHours;

    if (nextYearRequestedHours < 0) {
      currentYearAllowance.setTakenAnnual(currentYearTakenAnnual + nextYearRequestedHours);
      nextYearAllowance.setTakenAnnual(0);

      allowanceRepository.save(currentYearAllowance);
    } else {
      nextYearAllowance.setTakenAnnual(nextYearRequestedHours);
    }

    allowanceRepository.save(nextYearAllowance);
  }

  private void applyAnnual(Allowance currentYearAllowance, Allowance nextYearAllowance, int requestedHours) {
    var currentYearAnnual = currentYearAllowance.getTakenAnnual();
    var nextYearAnnual = nextYearAllowance.getTakenAnnual();

    var remainingAnnualHoursCurrentYear = calculateRemainingAnnualHoursWithoutPending(currentYearAllowance);
    var nextYearRequestedHours = requestedHours - remainingAnnualHoursCurrentYear + nextYearAnnual;

    if (nextYearRequestedHours > 0) {
      currentYearAllowance.setTakenAnnual(currentYearAnnual + remainingAnnualHoursCurrentYear);
      nextYearAllowance.setTakenAnnual(nextYearRequestedHours);

      allowanceRepository.save(nextYearAllowance);
    } else {
      currentYearAllowance.setTakenAnnual(currentYearAnnual + requestedHours);
    }

    allowanceRepository.save(currentYearAllowance);
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

  private List<Day> getWorkingDaysOnly(List<Day> days) {
    var workingDaysWithoutWeekend = getWorkingDaysWithoutWeekend(days);

    Set<Integer> requestYears = workingDaysWithoutWeekend.stream()
                                                         .map(Day::getDate)
                                                         .map(LocalDate::getYear)
                                                         .collect(Collectors.toCollection(LinkedHashSet::new));

    var yearFrom = requestYears.stream().findFirst().orElse(0);
    var yearTo = requestYears.stream().reduce((a, b) -> b).orElse(0);
    var startYearFrom = LocalDate.of(yearFrom, 1, 1);
    var endYearTo = LocalDate.of(yearTo, 12, 31);
    var publicHolidays = publicHolidayRepository.findAllByDateIsBetween(startYearFrom, endYearTo);

    return getWorkingDaysWithoutPublicHolidays(workingDaysWithoutWeekend, publicHolidays);
  }

  private List<Day> getWorkingDaysWithoutPublicHolidays(List<Day> workingDaysWithoutWeekend,
      List<PublicHoliday> publicHolidays) {
    return workingDaysWithoutWeekend.stream().filter(isWorkday(publicHolidays)).collect(Collectors.toList());
  }

  private Predicate<Day> isWorkday(List<PublicHoliday> publicHolidays) {
    return day -> publicHolidays.stream().noneMatch(publicHoliday -> publicHoliday.getDate().equals(day.getDate()));
  }

  private List<Day> getWorkingDaysWithoutWeekend(List<Day> days) {
    return days.stream().filter(isWorkday()).collect(Collectors.toList());
  }

  private Predicate<Day> isWorkday() {
    return day -> !(DayOfWeek.SATURDAY.equals(day.getDate().getDayOfWeek()) || DayOfWeek.SUNDAY.equals(
        day.getDate().getDayOfWeek()));
  }

  private int calculateHours(List<Day> days) {
    return days.stream().mapToInt(this::getHoursFromDay).sum();
  }

  private int getHoursFromDay(Day day) {
    return Duration.FULL_DAY.equals(day.getDuration()) ? FULL_DAY : HALF_DAY;
  }

  private void checkRemainingAnnualHours(Allowance currentYearAllowance, Allowance nextYearAllowance,
      int requestedHours) {

    var userEmail = currentYearAllowance.getUser().getEmail();
    var remainingHoursCurrentYear = calculateRemainingAnnualHours(currentYearAllowance);
    var remainingHoursNextYear = calculateNextYearRemainingAnnualHours(nextYearAllowance);

    if (requestedHours > remainingHoursCurrentYear + remainingHoursNextYear) {
      log.error("Insufficient hours: available {}, requested {}, for user with email {}", remainingHoursCurrentYear,
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

  private int calculateRemainingAnnualHoursWithoutPending(Allowance allowance) {
    var totalHours =
        allowance.getAnnual() + allowance.getBonus() + allowance.getCarriedOver() + allowance.getManualAdjust();
    var takenAnnual = allowance.getTakenAnnual();

    return totalHours - takenAnnual;
  }

  private int calculateNextYearRemainingAnnualHours(Allowance allowance) {
    var takenAnnual = allowance.getTakenAnnual();
    var pendingAnnual = allowance.getPendingAnnual();

    return FIVE_DAYS - takenAnnual - pendingAnnual;
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

  private void checkRemainingBonusHours(LeaveProfile leaveProfile, Allowance allowance, int requestedHours) {
    var totalBonus = leaveProfile.getMaxBonusDays();
    var takenBonus = allowance.getBonus();
    var remainingBonusHours = totalBonus - takenBonus;

    if (requestedHours > remainingBonusHours) {
      throw new InsufficientHoursException();
    }
  }

  /*public Map<String, Integer> getFirstAndLastAllowancesYear(User authUser) {
    List<Allowance> allowances = allowanceRepository.findAllByUserId(authUser.getId());
    Map<String, Integer> firstAndLastYear = new HashMap<>();

    IntSummaryStatistics firstAndLastAllowanceYear = allowances.stream()
                                                               .map((allowance -> allowance.getYear()))
                                                               .collect(Collectors.summarizingInt(Integer::intValue));

    firstAndLastYear.put("first", firstAndLastAllowanceYear.getMin());
    firstAndLastYear.put("last", firstAndLastAllowanceYear.getMax());

    return firstAndLastYear;
  }*/

}
