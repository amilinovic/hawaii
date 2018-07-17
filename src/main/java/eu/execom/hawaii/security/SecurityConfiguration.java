package eu.execom.hawaii.security;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableOAuth2Sso
@EnableWebSecurity
@EnableSwagger2
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .authorizeRequests()
        .antMatchers("/", "/welcome", "/signin", "/authentication", "/swagger-ui.html", "/swagger-resources/**",
            "/v2/**", "/webjars/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .logout()
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID")
        .logoutSuccessUrl("http://localhost:3000/")
        .permitAll();
  }

}
