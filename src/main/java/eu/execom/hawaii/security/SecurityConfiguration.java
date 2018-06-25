package eu.execom.hawaii.security;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableOAuth2Sso
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .antMatcher("/**")
        .authorizeRequests()
        .antMatchers("/", "/welcome", "/authentication")
        .permitAll()
        .antMatchers("/users").hasRole("HR_MANAGER")
        .anyRequest()
        .authenticated()
        .and()
        .logout()
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID")
        .logoutSuccessUrl("http://localhost:3000/")
        .permitAll();
  }

}
