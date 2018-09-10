package eu.execom.hawaii.configuration;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class SpringAsyncConfig {

  @Bean
  public Executor threadPoolTaskExecutor() {
    return new ThreadPoolTaskExecutor();
  }

}
