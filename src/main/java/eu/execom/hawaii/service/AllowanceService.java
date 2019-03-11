package eu.execom.hawaii.service;

import eu.execom.hawaii.dto.AllowanceForUserDto;
import eu.execom.hawaii.exceptions.InsufficientHoursException;
import eu.execom.hawaii.model.Allowance;
import eu.execom.hawaii.model.Day;
import eu.execom.hawaii.model.LeaveProfile;
import eu.execom.hawaii.model.PublicHoliday;
import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.AbsenceSubtype;
import eu.execom.hawaii.model.enumerations.AbsenceType;
import eu.execom.hawaii.model.enumerations.Duration;
import eu.execom.hawaii.repository.AllowanceRepository;
import eu.execom.hawaii.repository.PublicHolidayRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AllowanceService {

  private static final int HALF_DAY = 4;
  private static final int FULL_DAY = 8;
  private static final String ANNUAL = "annual";
  private static final String TRAINING = "training";
  private static final String BONUS = "bonus";

  private AllowanceRepository allowanceRepository;
  private PublicHolidayRepository publicHolidayRepository;

  @Autowired
  public AllowanceService(AllowanceRepository allowanceRepository, PublicHolidayRepository publicHolidayRepository) {
    this.allowanceRepository = allowanceRepository;
    this.publicHolidayRepository = publicHolidayRepository;
  }

  /**
   * Saves the provided Allowance to repository.
   *
   * @param allowance the Allowance entity to be persisted.
   * @return saved Allowance.
   */
  @Transactional
  public Allowance save(Allowance allowance) {
    return allowanceRepository.save(allowance);
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
      checkRemainingAnnualHours(currentYearAllowance, hours);
      if (requestCanceled) {
        cancelPendingAnnual(currentYearAllowance, hours);
      } else {
        applyPendingAnnual(currentYearAllowance, hours);
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
  public Allowance getByUserAndYear(Long userId, int year) {
    return allowanceRepository.findByUserIdAndYearYear(userId, year);
  }

  private void cancelPendingAnnual(Allowance currentYearAllowance, int requestedHours) {
    var currentYearPendingAnnual = currentYearAllowance.getPendingAnnual();
    var nextYearPendingInPreviousYear = 0;
    var nextYearRequestedHours = requestedHours;

    if (nextYearAllowanceExists(currentYearAllowance)) {
      var nextYearAllowance = getNextYearAllowance(currentYearAllowance);
      nextYearPendingInPreviousYear = nextYearAllowance.getPendingInPreviousYear();
      nextYearRequestedHours = nextYearPendingInPreviousYear + requestedHours;
      if (nextYearRequestedHours < 0) {
        currentYearAllowance.setPendingAnnual(currentYearPendingAnnual + nextYearRequestedHours);
        nextYearAllowance.setPendingInPreviousYear(0);
        allowanceRepository.save(currentYearAllowance);
      } else {
        nextYearAllowance.setPendingInPreviousYear(nextYearRequestedHours);
      }
      allowanceRepository.save(nextYearAllowance);
    } else {
      currentYearAllowance.setPendingAnnual(currentYearPendingAnnual + nextYearRequestedHours);
      allowanceRepository.save(currentYearAllowance);
    }
  }

  private void applyPendingAnnual(Allowance currentYearAllowance, int requestedHours) {
    var currentYearPendingAnnual = currentYearAllowance.getPendingAnnual();
    var remainingHoursCurrentYear = calculateRemainingAnnualHours(currentYearAllowance);
    var nextYearRequestedHours = requestedHours - remainingHoursCurrentYear;

    if (nextYearAllowanceExists(currentYearAllowance)) {
      var nextYearAllowance = getNextYearAllowance(currentYearAllowance);
      var nextYearPendingInPreviousYear = nextYearAllowance.getPendingInPreviousYear();
      nextYearRequestedHours += nextYearPendingInPreviousYear;
      if (nextYearRequestedHours > 0) {
        currentYearAllowance.setPendingAnnual(currentYearPendingAnnual + remainingHoursCurrentYear);
        nextYearAllowance.setPendingInPreviousYear(nextYearRequestedHours);
        allowanceRepository.save(nextYearAllowance);
      } else {
        currentYearAllowance.setPendingAnnual(currentYearPendingAnnual + requestedHours);
      }

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
            checkRemainingAnnualHours(currentYearAllowance, hours);
            if (requestCanceled) {
              cancelAnnual(currentYearAllowance, hours);
            } else {
              applyAnnual(currentYearAllowance, hours);
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

  private void cancelAnnual(Allowance currentYearAllowance, int requestedHours) {
    var currentYearTakenAnnual = currentYearAllowance.getTakenAnnual();

    if (nextYearAllowanceExists(currentYearAllowance)) {
      var nextYearAllowance = getNextYearAllowance(currentYearAllowance);
      var nextYearTakenInPreviousYear = nextYearAllowance.getTakenInPreviousYear();
      var nextYearRequestedHours = nextYearTakenInPreviousYear + requestedHours;
      if (nextYearRequestedHours < 0) {
        currentYearAllowance.setTakenAnnual(currentYearTakenAnnual + nextYearRequestedHours);
        nextYearAllowance.setTakenInPreviousYear(0);

        allowanceRepository.save(currentYearAllowance);
      } else {
        nextYearAllowance.setTakenInPreviousYear(nextYearRequestedHours);
      }

      allowanceRepository.save(nextYearAllowance);
    } else {
      currentYearAllowance.setTakenAnnual(
          currentYearTakenAnnual + requestedHours); // linija u kojoj je promenjen flow(ovako mi je logicno da treba )
      allowanceRepository.save(currentYearAllowance);
    }
  }

  private void applyAnnual(Allowance currentYearAllowance, int requestedHours) {
    var currentYearAnnual = currentYearAllowance.getTakenAnnual();
    var remainingAnnualHoursCurrentYear = calculateRemainingAnnualHoursWithoutPending(currentYearAllowance);
    var nextYearRequestedHours = requestedHours - remainingAnnualHoursCurrentYear;

    if (nextYearAllowanceExists(currentYearAllowance)) {
      var nextYearAllowance = getNextYearAllowance(currentYearAllowance);
      var nextYearTakenInPreviousYear = nextYearAllowance.getTakenInPreviousYear();
      nextYearRequestedHours += nextYearTakenInPreviousYear;

      if (nextYearRequestedHours > 0) {
        currentYearAllowance.setTakenAnnual(currentYearAnnual + remainingAnnualHoursCurrentYear);
        nextYearAllowance.setTakenInPreviousYear(nextYearRequestedHours);
        allowanceRepository.save(nextYearAllowance);
      } else {
        currentYearAllowance.setTakenAnnual(currentYearAnnual + requestedHours);
      }

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

  /**
   * Checks remaining annual hours that user has available for current and next year.
   * If user requested more hours than it is available exception is thrown.
   */
  private void checkRemainingAnnualHours(Allowance currentYearAllowance, int requestedHours) {

    var userEmail = currentYearAllowance.getUser().getEmail();
    var remainingHoursCurrentYear = calculateRemainingAnnualHours(currentYearAllowance);
    var remainingHoursNextYear = 0;
    if (nextYearAllowanceExists(currentYearAllowance)) {
      var nextYearAllowance = getNextYearAllowance(currentYearAllowance);
      remainingHoursNextYear = calculateNextYearRemainingAnnualHours(nextYearAllowance);
    }

    if (requestedHours > remainingHoursCurrentYear + remainingHoursNextYear) {
      logAndThrowInsufficientHoursException(remainingHoursCurrentYear, requestedHours, userEmail, ANNUAL);
    }
  }

  /**
   * Calculates available hours for use in current year for given user.
   * That is all available hours that user has
   * minus hours that he already spent, minus hours that are pending for approval and hours used in previous year.
   */
  public int calculateRemainingAnnualHours(Allowance allowance) {
    var totalHours =
        allowance.getAnnual() + allowance.getBonus() + allowance.getCarriedOver() + allowance.getManualAdjust();
    var takenAnnual = allowance.getTakenAnnual();
    var pendingAnnual = allowance.getPendingAnnual();
    var usedInPreviousYear = allowance.getTakenInPreviousYear() + allowance.getPendingInPreviousYear();

    return totalHours - takenAnnual - pendingAnnual - usedInPreviousYear;
  }

  private int calculateRemainingAnnualHoursWithoutPending(Allowance allowance) {
    var totalHours =
        allowance.getAnnual() + allowance.getBonus() + allowance.getCarriedOver() + allowance.getManualAdjust();
    var takenAnnual = allowance.getTakenAnnual();
    var usedInPreviousYear = allowance.getTakenInPreviousYear();

    return totalHours - takenAnnual - usedInPreviousYear;
  }

  /**
   * Calculates available hours for use in next year for given user.
   * Those hours are calculated based on spent annual hours plus hours that are pending for approval
   * and maximum allowed hours from next year that user can use in current year with is set based on users LeaveProfile.
   */
  private int calculateNextYearRemainingAnnualHours(Allowance allowance) {
    var maxAllowedAllowanceFromNextYear = allowance.getUser().getLeaveProfile().getMaxAllowanceFromNextYear();
    var allowanceFromNextYear = allowance.getPendingInPreviousYear() + allowance.getTakenInPreviousYear();

    return maxAllowedAllowanceFromNextYear - allowanceFromNextYear;
  }

  private void checkRemainingTrainingHours(Allowance allowance, int requestedHours) {
    var userEmail = allowance.getUser().getEmail();
    var remainingHours = calculateRemainingTrainingHours(allowance);
    if (requestedHours > remainingHours) {
      logAndThrowInsufficientHoursException(remainingHours, requestedHours, userEmail, TRAINING);
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
      logAndThrowInsufficientHoursException(remainingBonusHours, requestedHours, allowance.getUser().getEmail(), BONUS);
    }
  }

  private void logAndThrowInsufficientHoursException(int remainingHours, int requestedHours, String userEmail,
      String leaveType) {
    log.error("Insufficient hours: available '{}', requested '{}', for user with email '{}'", remainingHours,
        requestedHours, userEmail);
    throw new InsufficientHoursException(leaveType);
  }

  private boolean nextYearAllowanceExists(Allowance allowance) {
    var year = allowance.getYear().getYear() + 1;
    var userId = allowance.getUser().getId();
    return allowanceRepository.existsByUserIdAndYearYear(userId, year);
  }

  private Allowance getNextYearAllowance(Allowance allowance) {
    var year = allowance.getYear().getYear() + 1;
    var userId = allowance.getUser().getId();

    return allowanceRepository.findByUserIdAndYearYear(userId, year);
  }

  public AllowanceForUserDto getAllowancesForUser(User user) {
    AllowanceForUserDto allowanceForUserDto = new AllowanceForUserDto();

    var yearOfRequest = LocalDate.now().getYear();
    var currentYearAllowance = getByUserAndYear(user.getId(), yearOfRequest);
    var nextYearAllowance = getByUserAndYear(user.getId(), yearOfRequest + 1);

    allowanceForUserDto.setRemainingAnnualHours(calculateRemainingAnnualHours(currentYearAllowance));
    allowanceForUserDto.setNextYearRemainingAnnualHours(calculateNextYearRemainingAnnualHours(nextYearAllowance));
    allowanceForUserDto.setRemainingTrainingHours(calculateRemainingTrainingHours(currentYearAllowance));

    return allowanceForUserDto;
  }

  public Map<String, Integer> getFirstAndLastAllowancesYear(User authUser) {
    List<Allowance> allowances = allowanceRepository.findAllByUserId(authUser.getId());
    Map<String, Integer> firstAndLastYear = new HashMap<>();

    IntSummaryStatistics firstAndLastAllowanceYear = allowances.stream()
                                                               .map((allowance -> allowance.getYear().getYear()))
                                                               .collect(Collectors.summarizingInt(Integer::intValue));

    firstAndLastYear.put("first", firstAndLastAllowanceYear.getMin());
    firstAndLastYear.put("last", firstAndLastAllowanceYear.getMax());

    return firstAndLastYear;
  }
}
