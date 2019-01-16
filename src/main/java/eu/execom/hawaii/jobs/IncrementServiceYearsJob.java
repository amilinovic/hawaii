package eu.execom.hawaii.jobs;

import eu.execom.hawaii.model.LeaveProfile;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.LeaveProfileType;
import eu.execom.hawaii.model.enumerations.UserStatusType;
import eu.execom.hawaii.repository.LeaveProfileRepository;
import eu.execom.hawaii.service.EmailService;
import eu.execom.hawaii.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class IncrementServiceYearsJob {

  private static final int UPDATE_THRESHOLD = 5;
  private static final MonthDay FEBRUARY_TWENTY_NINE = MonthDay.of(2, 29);

  private LeaveProfileRepository leaveProfileRepository;
  private EmailService emailService;
  private UserService userService;

  @Autowired
  public IncrementServiceYearsJob(LeaveProfileRepository leaveProfileRepository, EmailService emailService,
      UserService userService) {
    this.leaveProfileRepository = leaveProfileRepository;
    this.emailService = emailService;
    this.userService = userService;
  }

  /**
   * Check every day at 6:00 am to see if years of service needs to be incremented.
   * If user reaches 5, 10, 15 years... Update leave profile, save, send update mail.
   */
  @Scheduled(cron = "0 0 6 * * *")
  @Transactional
  public void addServiceYearsToUser() {
    List<User> users = userService.findAllByUserStatusType(Collections.singletonList(UserStatusType.ACTIVE));

    Supplier<Stream<User>> userStream = () -> users.stream()
                                                   .filter(user -> shouldUpdateYearsOfService(
                                                       MonthDay.from(user.getStartedWorkingDate()),
                                                       user.getYearsOfServiceSetOnDate()));
    userStream.get().forEach(user -> {
      user.setYearsOfService(user.getYearsOfService() + 1);
      user.setYearsOfServiceSetOnDate(LocalDate.now());
      userService.save(user);
    });
    userStream.get()
              .filter(user -> user.getLeaveProfile().isUpgradeable())
              .filter(shouldUpdateLeaveProfile())
              .forEach(user -> {
                var leaveProfile = user.getLeaveProfile();
                user.setLeaveProfile(findNextLeaveProfile(leaveProfile));
                emailService.createLeaveProfileUpdateEmailAndSendForApproval(user);
                userService.updateAllowanceForUserOnLeaveProfileUpdate(user, leaveProfile);
              });
  }

  private boolean shouldUpdateYearsOfService(MonthDay startedWorkingDate, LocalDate yearsOfServiceSetOnDate) {
    LocalDate todaysDate = LocalDate.now();

    if (startedWorkingDate.equals(FEBRUARY_TWENTY_NINE) && !todaysDate.isLeapYear()) {
      return MonthDay.of(2, 28).equals(MonthDay.from(todaysDate));
    } else {
      return startedWorkingDate.equals(MonthDay.from(todaysDate)) || yearsOfServiceSetOnDate.isBefore(
          todaysDate.minusYears(1));
    }
  }

  private Predicate<User> shouldUpdateLeaveProfile() {
    return user -> user.getYearsOfService() % UPDATE_THRESHOLD == 0;
  }

  private LeaveProfile findNextLeaveProfile(LeaveProfile currentLeaveProfile) {
    LeaveProfileType nextLeaveProfileType;
    var leaveProfileType = currentLeaveProfile.getLeaveProfileType();

    switch (leaveProfileType) {
      case ZERO_TO_FIVE_YEARS:
        nextLeaveProfileType = LeaveProfileType.FIVE_TO_TEN_YEARS;
        break;
      case FIVE_TO_TEN_YEARS:
        nextLeaveProfileType = LeaveProfileType.TEN_TO_FIFTEEN_YEARS;
        break;
      case TEN_TO_FIFTEEN_YEARS:
        nextLeaveProfileType = LeaveProfileType.FIFTEEN_TO_TWENTY_YEARS;
        break;
      case FIFTEEN_TO_TWENTY_YEARS:
        nextLeaveProfileType = LeaveProfileType.TWENTY_TO_TWENTYFIVE_YEARS;
        break;
      case TWENTY_TO_TWENTYFIVE_YEARS:
        nextLeaveProfileType = LeaveProfileType.TWENTYFIVE_TO_THIRTY_YEARS;
        break;
      case TWENTYFIVE_TO_THIRTY_YEARS:
        nextLeaveProfileType = LeaveProfileType.THIRTY_TO_THIRTYFIVE_YEARS;
        break;
      case THIRTY_TO_THIRTYFIVE_YEARS:
        nextLeaveProfileType = LeaveProfileType.THIRTYFIVE_TO_FORTY_YEARS;
        break;
      default:
        return currentLeaveProfile;
    }

    return leaveProfileRepository.findOneByLeaveProfileType(nextLeaveProfileType);
  }
}
