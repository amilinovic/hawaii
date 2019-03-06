package eu.execom.hawaii;

import eu.execom.hawaii.repository.CustomizedTeamRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomizedTeamRepositoryImpl.class)
@EnableCaching
@EnableScheduling
public class HawaiiApplication {

  public static void main(String[] args) {
    SpringApplication.run(HawaiiApplication.class, args);
  }

}
