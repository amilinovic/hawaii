package eu.execom.hawaii.jobs;

import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.UserStatusType;
import eu.execom.hawaii.repository.UserRepository;
import eu.execom.hawaii.service.EmailService;
import eu.execom.hawaii.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class IncrementServiceYearsJob {

  private static final int FIVE_YEARS = 5;
  private static final int TEN_YEARS = 10;
  private static final int FIFTEEN_YEARS = 15;
  private static final MonthDay FEBRUARY_TWENTY_NINE = MonthDay.of(2, 29);

  private UserRepository userRepository;
  private EmailService emailService;
  private UserService userService;

  @Autowired
  public IncrementServiceYearsJob(UserRepository userRepository, EmailService emailService, UserService userService) {
    this.userRepository = userRepository;
    this.emailService = emailService;
    this.userService = userService;
  }

  /**
   * Check every day at 6:00 am to see if years of service needs to be incremented.
   * If user reaches 5, 10, 15 years... Update leave profile, save, send update mail.
   */
  @Scheduled(cron = "0 0 6 * * *")
  public void addServiceYearsToUser() {
    List<User> users = userRepository.findAllByUserStatusTypeIn(Collections.singletonList(UserStatusType.ACTIVE));

    Supplier<Stream<User>> userStream = () -> users.stream()
                                                   .filter(user -> startedWorkingToday(
                                                       MonthDay.from(user.getStartedWorkingDate())));

    userStream.get()
              .forEach(user -> {
                       user.setYearsOfService(user.getYearsOfService() + 1);
                       userRepository.save(user);
              });

    userStream.get()
              .filter(shouldUpdateLeaveProfile())
              .forEach(user -> {
                       user.getLeaveProfile().setId(user.getLeaveProfile().getId() + 1);
                       emailService.createLeaveProfileUpdateEmailAndSendForApproval(user);
                       userService.updateAllowanceForUserOnLeaveProfileUpdate(user);
              });
  }

  private boolean startedWorkingToday(MonthDay startedWorkingDate) {
    if ((startedWorkingDate == FEBRUARY_TWENTY_NINE) && !LocalDate.now().isLeapYear()) {
      return MonthDay.of(2, 28).equals(MonthDay.from(LocalDate.now()));
    } else {
      return startedWorkingDate.equals(MonthDay.from(LocalDate.now()));
    }
  }

  private Predicate<User> shouldUpdateLeaveProfile() {
    return user -> user.getYearsOfService() == FIVE_YEARS ||
                   user.getYearsOfService() == TEN_YEARS  ||
                   user.getYearsOfService() == FIFTEEN_YEARS;
  }
}
