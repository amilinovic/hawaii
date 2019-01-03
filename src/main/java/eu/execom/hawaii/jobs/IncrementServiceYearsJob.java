package eu.execom.hawaii.jobs;

import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.UserStatusType;
import eu.execom.hawaii.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Collections;
import java.util.List;

public class IncrementServiceYearsJob {

  private UserRepository userRepository;

  @Autowired
  public IncrementServiceYearsJob(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Each active user receives increment of one year of service on every year, on 1st of January
   */
  @Scheduled(cron = "0 1 1 * * *")
  public void addServiceYearsToUser() {
    List<User> users = userRepository.findAllByUserStatusTypeIn(Collections.singletonList(UserStatusType.ACTIVE));
    users.forEach(user -> {
      user.setYearsOfService(user.getYearsOfService() + 1);
      userRepository.save(user);
    });
  }
}
