package eu.execom.hawaii.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import eu.execom.hawaii.model.Absence;
import eu.execom.hawaii.model.Allowance;
import eu.execom.hawaii.model.Day;
import eu.execom.hawaii.model.LeaveProfile;
import eu.execom.hawaii.model.PublicHoliday;
import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.Team;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.AbsenceSubtype;
import eu.execom.hawaii.model.enumerations.AbsenceType;
import eu.execom.hawaii.model.enumerations.Duration;
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
    user.setId(1L);
    user.setTeam(team);
    user.setLeaveProfile(leaveProfile());
    user.setFullName("Aria Stark");
    user.setEmail("aria.stark@gmail.com");
    user.setUserRole(UserRole.HR_MANAGER);
    user.setJobTitle("Developer");
    user.setActive(true);
    user.setApproverTeams(new ArrayList<>());
    user.setRequests(new ArrayList<>());
    user.setAllowances(new ArrayList<>());

    return user;
  }

  static User approver() {
    var approver = new User();
    approver.setId(3L);
    approver.setTeam(team());
    approver.setLeaveProfile(leaveProfile());
    approver.setFullName("Bruce Wayne");
    approver.setEmail("bruce.wayne@execom.eu");
    approver.setUserRole(UserRole.HR_MANAGER);
    approver.setJobTitle("Developer");
    approver.setActive(true);
    approver.setApproverTeams(new ArrayList<>());
    approver.setRequests(new ArrayList<>());
    approver.setAllowances(new ArrayList<>());

    return approver;
  }

  static LeaveProfile leaveProfile() {
    var leaveProfile = new LeaveProfile();
    leaveProfile.setId(1L);
    leaveProfile.setName("Default");
    leaveProfile.setEntitlement(160);
    leaveProfile.setMaxCarriedOver(40);
    leaveProfile.setMaxBonusDays(40);
    leaveProfile.setTraining(16);
    leaveProfile.setComment("No comment");
    leaveProfile.setUsers(new ArrayList<>());

    return leaveProfile;
  }

  static Request request(Absence absence, List<Day> days) {
    var request = new Request();
    request.setId(1L);
    request.setUser(user(team()));
    request.getUser().setId(1L);
    request.setAbsence(absence);
    request.setRequestStatus(RequestStatus.PENDING);
    request.setReason("My request reason");
    request.setDays(days);

    return request;
  }

  static Allowance allowance(User user) {
    var allowance = new Allowance();
    allowance.setId(1L);
    allowance.setUser(user);
    allowance.setYear(2018);
    allowance.setAnnual(160);
    allowance.setTakenAnnual(0);
    allowance.setSickness(0);
    allowance.setBonus(0);
    allowance.setCarriedOver(40);
    allowance.setManualAdjust(0);
    allowance.setTraining(16);
    allowance.setTakenTraining(0);

    return allowance;
  }

  static PublicHoliday publicholiday() {
    var publicHoliday = new PublicHoliday();
    publicHoliday.setActive(true);
    publicHoliday.setName("New year");
    publicHoliday.setDate(LocalDate.of(2018, 1, 5));

    return publicHoliday;
  }

  static Absence absence() {
    var absence = new Absence();
    absence.setId(2L);
    absence.setAbsenceType(AbsenceType.BONUS_DAYS);
    absence.setName("Training");
    absence.setComment("Description");
    absence.setActive(true);
    absence.setIconUrl("icons/training.png");
    absence.setLeaveRequests(new ArrayList<>());

    return absence;
  }

  static Absence absenceAnnual() {
    var absence = new Absence();
    absence.setId(1L);
    absence.setAbsenceType(AbsenceType.DEDUCTED_LEAVE);
    absence.setAbsenceSubtype(AbsenceSubtype.ANNUAL);
    absence.setName("Annual leave");
    absence.setComment("Description");
    absence.setActive(true);
    absence.setIconUrl("icons/vacation.png");
    absence.setLeaveRequests(new ArrayList<>());

    return absence;
  }

  static Absence absenceTraining() {
    var absence = new Absence();
    absence.setId(3L);
    absence.setAbsenceType(AbsenceType.DEDUCTED_LEAVE);
    absence.setAbsenceSubtype(AbsenceSubtype.TRAINING);
    absence.setName("Training");
    absence.setComment("Description");
    absence.setActive(true);
    absence.setIconUrl("icons/training.png");
    absence.setLeaveRequests(new ArrayList<>());

    return absence;
  }

  static Day day(LocalDate date) {
    var day = new Day();
    day.setRequest(new Request());
    day.setDate(date);
    day.setDuration(Duration.FULL_DAY);

    return day;
  }

}
