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
import java.time.Period;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class IncrementServiceYearsJob {

  private static final int UPDATE_THRESHOLD = 5;

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

    LocalDate todaysDate = LocalDate.now();

    users.stream()
         .filter(yearsOfServiceUpToDate(todaysDate))
         .forEach(user -> {
           incrementYearsOfService(user, todaysDate);
           updateLeaveProfile(user);
         });
  }

  private Predicate<User> yearsOfServiceUpToDate(LocalDate todaysDate) {
    return user -> Period.between(user.getStartedWorkingDate(), todaysDate).getYears() != user.getYearsOfService();
  }

  private void incrementYearsOfService(User user, LocalDate todaysDate) {
    user.setYearsOfService(Period.between(user.getStartedWorkingDate(), todaysDate).getYears());
    userService.save(user);
  }

  private void updateLeaveProfile(User user) {
    if (user.getLeaveProfile().isUpgradeable() && shouldUpdateLeaveProfile(user)) {
      var previousLeaveProfile = user.getLeaveProfile();
      user.setLeaveProfile(findNextLeaveProfile(previousLeaveProfile));
      emailService.createLeaveProfileUpdateEmailAndSendForApproval(user);
      userService.updateAllowanceForUserOnLeaveProfileUpdate(user, previousLeaveProfile);
    }
  }

  private boolean shouldUpdateLeaveProfile(User user) {
    return user.getYearsOfService() % UPDATE_THRESHOLD == 0;
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
