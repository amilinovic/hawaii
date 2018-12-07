package eu.execom.hawaii.service;

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
  public YearService(YearRepository yearRepository, UserRepository userRepository, AllowanceRepository allowanceRepository){
    this.yearRepository = yearRepository;
    this.userRepository = userRepository;
    this.allowanceRepository = allowanceRepository;
  }

  public Year saveYear(Year year){
    return yearRepository.save(year);
  }

  public void createAllowanceOnCreateYear(Year createdYear){
    List<User> activeUsers = userRepository.findAllByUserStatusTypeIn(Collections.singletonList(UserStatusType.ACTIVE));
    for(User u: activeUsers){
      LeaveProfile leaveProfile = u.getLeaveProfile();
      createAllowance(createdYear, leaveProfile, u);
    }

  }

  public void createAllowance(Year createdYear, LeaveProfile leaveProfile, User user){
   /* Allowance allowance = new Allowance();
    allowance.setUser(user);
    allowance.setYear(createdYear);
    allowance.setAnnual(leaveProfile.getEntitlement());
    allowance.setTraining(leaveProfile.getTraining());
    allowanceRepository.save(allowance);*/

//    return allowance;
  }
}
