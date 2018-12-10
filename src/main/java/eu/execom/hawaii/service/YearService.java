package eu.execom.hawaii.service;

import eu.execom.hawaii.model.Year;
import eu.execom.hawaii.repository.YearRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YearService {

  private YearRepository yearRepository;

  @Autowired
  public YearService(YearRepository yearRepository) {
    this.yearRepository = yearRepository;
  }

  public List<Year> getAll() {
    return yearRepository.findAll();
  }

  public Year getById(Long id) {
    return yearRepository.getOne(id);
  }

  public Year saveYear(Year year) {
    return yearRepository.save(year);
  }

  public void delete(Long id) {
    var year = getById(id);
    year.setActive(false);
    yearRepository.deleteById(id);
  }
}