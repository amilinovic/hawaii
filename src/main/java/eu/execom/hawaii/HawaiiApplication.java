package eu.execom.hawaii;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class HawaiiApplication {

  public static void main(String[] args) {
    SpringApplication.run(HawaiiApplication.class, args);
  }

}
