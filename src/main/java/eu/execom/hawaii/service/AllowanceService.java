package eu.execom.hawaii.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.execom.hawaii.model.Allowance;
import eu.execom.hawaii.model.Day;
import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.Duration;
import eu.execom.hawaii.repository.AllowanceRepository;

@Service
public class AllowanceService {

  private AllowanceRepository allowanceRepository;

  @Autowired
  public AllowanceService(AllowanceRepository allowanceRepository) {
    this.allowanceRepository = allowanceRepository;
  }

  public Allowance getByUser(User user) {
    return allowanceRepository.findByUser(user);
  }

  public Allowance save(Allowance allowance) {
    return allowanceRepository.save(allowance);
  }

  void applyRequest(Request request) {
    var allowance = getByUser(request.getUser());
    var absence = request.getAbsence();

    switch (absence.getAbsenceType()) {
      case LEAVE:
        if (absence.isDeducted()) {
          deductAnnual(allowance, request);
        }
        break;
      case SICKNESS:
        incrementSickness(allowance, request);
        break;
      case BONUS_DAYS:
        incrementBonus(allowance, request);
        break;
    }
  }

  private void deductAnnual(Allowance allowance, Request request) {
    // Deduct annual from request days.
  }

  private void incrementSickness(Allowance allowance, Request request) {
    // increment sickness from request days.
  }

  private void incrementBonus(Allowance allowance, Request request) {
    // increment bonus from request days.
  }

  private int calculateDays(List<Day> days) {
    int numberOfHours = 1;
    days.stream().forEach(day -> increment(day, numberOfHours));

    return numberOfHours;
  }

  private void increment(Day day, int hours) {
    if (day.getDuration().equals(Duration.FULL_DAY)) {
      hours = hours + 8;
    }
  }

}
