package eu.execom.hawaii.jobs;

import eu.execom.hawaii.model.Allowance;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.UserStatusType;
import eu.execom.hawaii.repository.AllowanceRepository;
import eu.execom.hawaii.repository.UserRepository;
import eu.execom.hawaii.service.AllowanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class CarryOverVacationJob {

  private AllowanceRepository allowanceRepository;
  private UserRepository userRepository;
  private AllowanceService allowanceService;

  @Autowired
  public CarryOverVacationJob(AllowanceRepository allowanceRepository, AllowanceService allowanceService,
      UserRepository userRepository) {
    this.allowanceRepository = allowanceRepository;
    this.allowanceService = allowanceService;
    this.userRepository = userRepository;
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
    List<User> users = userRepository.findAllByUserStatusTypeIn(Collections.singletonList(UserStatusType.ACTIVE));
    int currentYear = LocalDate.now().getYear();
    users.forEach(user -> {
      Allowance thisYearAllowance = allowanceRepository.findByUserIdAndYear(user.getId(), currentYear);
      Allowance lastYearAllowance = allowanceRepository.findByUserIdAndYear(user.getId(), currentYear - 1);
      int remainingHours = allowanceService.calculateRemainingAnnualHours(lastYearAllowance);

      if (remainingHours > user.getLeaveProfile().getMaxCarriedOver()) {
        thisYearAllowance.setCarriedOver(user.getLeaveProfile().getMaxCarriedOver());
      } else {
        thisYearAllowance.setCarriedOver(remainingHours);
      }
      allowanceRepository.save(thisYearAllowance);
    });
  }
}
