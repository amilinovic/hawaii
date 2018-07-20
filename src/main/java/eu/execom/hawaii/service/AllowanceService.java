package eu.execom.hawaii.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.execom.hawaii.model.Allowance;
import eu.execom.hawaii.model.User;
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
}
