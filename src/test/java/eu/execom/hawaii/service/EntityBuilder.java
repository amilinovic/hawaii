package eu.execom.hawaii.service;

import java.time.LocalDate;
import java.util.ArrayList;

import eu.execom.hawaii.model.Allowance;
import eu.execom.hawaii.model.LeaveProfile;
import eu.execom.hawaii.model.PublicHoliday;
import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.Team;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.absence.Absence;
import eu.execom.hawaii.model.enumerations.RequestStatus;
import eu.execom.hawaii.model.enumerations.UserRole;

public class EntityBuilder {

  static Team team() {
    var team = new Team();
    team.setName("My team1");
    team.setActive(true);
    team.setEmails("example@example.com");
    team.setUsers(new ArrayList<>());
    team.setTeamApprovers(new ArrayList<>());

    return team;
  }

  static User user(Team team) {
    var user = new User();
    user.setTeam(team);
    user.setLeaveProfile(leaveProfile());
    user.setFullName("Aria Stark");
    user.setEmail("aria.stark@gmail.com");
    user.setUserRole(UserRole.HR_MANAGER);
    user.setJobTitle("Developer");
    user.setActive(true);
    user.setTeamApprovers(new ArrayList<>());
    user.setRequests(new ArrayList<>());
    user.setAllowances(new ArrayList<>());

    return user;
  }

  static LeaveProfile leaveProfile() {
    var leaveProfile = new LeaveProfile();
    leaveProfile.setName("Default");
    leaveProfile.setEntitlement(5);
    leaveProfile.setMaxCarriedOver(5);
    leaveProfile.setTraining(2);
    leaveProfile.setComment("No comment");
    leaveProfile.setPublicHolidays(new ArrayList<>());
    leaveProfile.setUsers(new ArrayList<>());

    return leaveProfile;
  }

  static Request request(Absence absence) {
    var request = new Request();
    request.setUser(user(team()));
    request.setApprover(user(team()));
    request.setAbsence(absence);
    request.setRequestStatus(RequestStatus.APPROVED);
    request.setReason("My request reason");
    request.setDays(new ArrayList<>());

    return request;
  }

  static Allowance allowance(User user) {
    var allowance = new Allowance();
    allowance.setUser(user);
    allowance.setYear(2018);
    allowance.setAnnual(20);
    allowance.setTaken(5);
    allowance.setSickness(3);
    allowance.setBonus(2);
    allowance.setCarriedOver(5);
    allowance.setManualAdjust(2);

    return allowance;
  }

  static PublicHoliday publicholiday() {
    var publicHoliday = new PublicHoliday();
    publicHoliday.setLeaveProfile(leaveProfile());
    publicHoliday.setActive(true);
    publicHoliday.setName("New year");
    publicHoliday.setDate(LocalDate.of(2018, 1, 1));

    return publicHoliday;
  }

}
