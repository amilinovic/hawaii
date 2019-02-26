package eu.execom.hawaii.jobs;

import eu.execom.hawaii.model.Allowance;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.UserStatusType;
import eu.execom.hawaii.service.AllowanceService;
import eu.execom.hawaii.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class CarryOverVacationJob {

  private UserService userService;
  private AllowanceService allowanceService;

  @Autowired
  public CarryOverVacationJob(UserService userService, AllowanceService allowanceService) {
    this.userService = userService;
    this.allowanceService = allowanceService;
  }

  /**
   * Job that is happening every year on the 1st of January, one second after midnight
   * For each active user it looks for any annual that is left in the previous year
   * If there is some, it carries it over into the new year.
   * Checks maximum amount of hours that can be carried over to new year based upon
   * users leave profile and if it exceeds given amount, it's caped to that value.
   */
  @Scheduled(cron = "1 0 0 1 1 *", zone = "Europe/Belgrade")
  public void addCarriedOverToUsers() {
    List<User> users = userService.findAllByUserStatusType(Collections.singletonList(UserStatusType.ACTIVE));
    var lastYear = LocalDate.now().minusYears(1).getYear();
    users.forEach(user -> saveCarriedOver(user, lastYear));
  }

  private void saveCarriedOver(User user, int lastYear) {
    Allowance thisYearAllowance = allowanceService.getByUserAndYear(user.getId(), lastYear + 1);
    thisYearAllowance.setCarriedOver(getCarriedOver(user, lastYear));

    allowanceService.save(thisYearAllowance);
  }

  private int getCarriedOver(User user, int lastYear) {
    int remainingLastYearHours = getRemainingHours(user, lastYear);
    int maxCarriedOverThreshold = user.getLeaveProfile().getMaxCarriedOver();

    return (remainingLastYearHours > maxCarriedOverThreshold) ? maxCarriedOverThreshold : remainingLastYearHours;
  }

  private int getRemainingHours(User user, int lastYear) {
    Allowance lastYearAllowance = allowanceService.getByUserAndYear(user.getId(), lastYear);

    return allowanceService.calculateRemainingAnnualHours(lastYearAllowance);
  }

}
