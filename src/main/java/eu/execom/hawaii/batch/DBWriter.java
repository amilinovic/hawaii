package eu.execom.hawaii.batch;

import eu.execom.hawaii.model.User;
import eu.execom.hawaii.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DBWriter implements ItemWriter<User> {

  private UserRepository userRepository;

  @Autowired
  public DBWriter(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void write(List<? extends User> users) throws Exception {
    users.forEach(user -> log.info("Data saved for user: " + user));

    userRepository.saveAll(users);
  }

}
