package eu.execom.hawaii.batch;

import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.batch.UserImport;
import eu.execom.hawaii.model.enumerations.LeaveProfileType;
import eu.execom.hawaii.model.enumerations.UserRole;
import eu.execom.hawaii.model.enumerations.UserStatusType;
import eu.execom.hawaii.repository.LeaveProfileRepository;
import eu.execom.hawaii.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class Processor implements ItemProcessor<UserImport, User> {

  private static final Map<String, LeaveProfileType> LEAVE_PROFILE_TYPES = new HashMap<>();
  private static final Map<String, UserStatusType> USER_TYPES = new HashMap<>();

  private TeamRepository teamRepository;
  private LeaveProfileRepository leaveProfileRepository;

  @Autowired
  public Processor(TeamRepository teamRepository, LeaveProfileRepository leaveProfileRepository) {
    this.teamRepository = teamRepository;
    this.leaveProfileRepository = leaveProfileRepository;
  }

  @PostConstruct
  public void init() {
    LEAVE_PROFILE_TYPES.put("Employees (3 days working)", LeaveProfileType.EMPLOYEES_3_DAYS_WORKING);
    LEAVE_PROFILE_TYPES.put("Employees (4 days working)", LeaveProfileType.EMPLOYEES_4_DAYS_WORKING);
    LEAVE_PROFILE_TYPES.put("0 - 5 Years of Service", LeaveProfileType.ZERO_TO_FIVE_YEARS);
    LEAVE_PROFILE_TYPES.put("5 - 10 Years of Service", LeaveProfileType.FIVE_TO_TEN_YEARS);
    LEAVE_PROFILE_TYPES.put("10 - 15 Years of Service", LeaveProfileType.TEN_TO_FIFTEEN_YEARS);
    LEAVE_PROFILE_TYPES.put("15 - 20 Years of Service", LeaveProfileType.FIFTEEN_TO_TWENTY_YEARS);
    LEAVE_PROFILE_TYPES.put("20 - 25 Years of Service", LeaveProfileType.TWENTY_TO_TWENTYFIVE_YEARS);
    LEAVE_PROFILE_TYPES.put("25 - 30 Years of Service", LeaveProfileType.TWENTYFIVE_TO_THIRTY_YEARS);
    LEAVE_PROFILE_TYPES.put("30 - 35 Years of Service", LeaveProfileType.THIRTY_TO_THIRTYFIVE_YEARS);
    LEAVE_PROFILE_TYPES.put("35 - 40 Years of Service", LeaveProfileType.THIRTYFIVE_TO_FORTY_YEARS);

    USER_TYPES.put("active", UserStatusType.ACTIVE);
    USER_TYPES.put("inactive", UserStatusType.INACTIVE);
    USER_TYPES.put("deleted", UserStatusType.DELETED);
  }

  @Override
  public User process(UserImport userImport) throws Exception {
    User user = createUserFromImport(userImport);
    log.debug("Converting '{}' into '{}'.", userImport, user);

    return user;
  }

  private User createUserFromImport(UserImport userImport) {
    LeaveProfileType leaveProfileType = LEAVE_PROFILE_TYPES.get(userImport.getLeaveProfile());
    UserStatusType userStatusType = USER_TYPES.get(userImport.getStatus());

    User user = new User();
    user.setFullName(userImport.getFirstName() + " " + userImport.getLastName());
    user.setEmail(userImport.getEmail());
    user.setUserRole(UserRole.USER);
    user.setJobTitle("Developer");
    user.setStartedWorkingDate(userImport.getContinuousStartDate());
    user.setStartedWorkingAtExecomDate(userImport.getStartDate());
    user.setStoppedWorkingAtExecomDate(userImport.getEndDate());
    user.setTeam(teamRepository.findByName(userImport.getTeam()));
    user.setLeaveProfile(leaveProfileRepository.findOneByLeaveProfileType(leaveProfileType));
    user.setUserStatusType(userStatusType);
    user.setYearsOfService(getYearsOfService(user));

    return user;
  }

  private int getYearsOfService(User user) {
    return Period.between(user.getStartedWorkingDate(), user.getStartedWorkingAtExecomDate()).getYears();
  }

}
