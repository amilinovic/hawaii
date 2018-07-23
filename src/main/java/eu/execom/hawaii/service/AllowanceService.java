package eu.execom.hawaii.service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

  Allowance getByUser(User user) {
    return allowanceRepository.findByUser(user);
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
    allowance.setAnnual(allowance.getAnnual() - calculateDays(request.getDays()));
    allowanceRepository.save(allowance);
  }

  private void incrementSickness(Allowance allowance, Request request) {
    allowance.setSickness(allowance.getSickness() + calculateDays(request.getDays()));
    allowanceRepository.save(allowance);
  }

  private void incrementBonus(Allowance allowance, Request request) {
    allowance.setBonus(allowance.getBonus() + calculateDays(request.getDays()));
    allowanceRepository.save(allowance);
  }

  private int calculateDays(List<Day> days) {
    AtomicInteger numberOfHours = new AtomicInteger(0);
    days.forEach(day -> increment(day, numberOfHours));

    return numberOfHours.get();
  }

  private void increment(Day day, AtomicInteger numberOfHours) {
    if (day.getDuration().equals(Duration.FULL_DAY)) {
      numberOfHours.set(numberOfHours.get() + 8);
    } else {
      numberOfHours.set(numberOfHours.get() + 4);
    }
  }

}
