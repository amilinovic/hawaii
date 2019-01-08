package eu.execom.hawaii.service;

import eu.execom.hawaii.model.Allowance;
import eu.execom.hawaii.model.LeaveProfile;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.Year;
import eu.execom.hawaii.model.enumerations.UserStatusType;
import eu.execom.hawaii.repository.AllowanceRepository;
import eu.execom.hawaii.repository.UserRepository;
import eu.execom.hawaii.repository.YearRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class YearService {

  private YearRepository yearRepository;
  private UserRepository userRepository;
  private AllowanceRepository allowanceRepository;

  @Autowired
  public YearService(YearRepository yearRepository, UserRepository userRepository,
      AllowanceRepository allowanceRepository) {
    this.yearRepository = yearRepository;
    this.userRepository = userRepository;
    this.allowanceRepository = allowanceRepository;
  }

  public List<Year> getAll() {
    return yearRepository.findAll();
  }

  public Year getById(Long id) {
    return yearRepository.getOne(id);
  }

  public Year findOneByYear(int year) {
    return yearRepository.findOneByYear(year);
  }

  public Year save(Year year) {
    return yearRepository.save(year);
  }

  public void delete(Long id) {
    var year = getById(id);
    year.setActive(false);
    yearRepository.save(year);
  }

  public void createAllowanceOnCreateYear(Year createdYear) {
    List<User> activeUsers = userRepository.findAllByUserStatusTypeIn(Collections.singletonList(UserStatusType.ACTIVE));
    for (User user : activeUsers) {
      LeaveProfile leaveProfile = user.getLeaveProfile();
      var userAllowances = user.getAllowances();
      Allowance allowance = createAllowance(createdYear, leaveProfile, user);
      userAllowances.add(allowance);
      userRepository.save(user);
    }
  }

  private Allowance createAllowance(Year createdYear, LeaveProfile leaveProfile, User user) {
    Allowance allowance = new Allowance();
    allowance.setUser(user);
    allowance.setYear(createdYear);
    allowance.setAnnual(leaveProfile.getEntitlement());
    allowance.setTraining(leaveProfile.getTraining());

    return allowance;
  }
}
