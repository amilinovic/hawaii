package eu.execom.hawaii.security;

import static eu.execom.hawaii.security.AuthenticationFilter.AUTHENTICATION_TOKEN_KEY;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import eu.execom.hawaii.model.User;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableOAuth2Sso
@EnableWebSecurity
@EnableSwagger2
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //@formatter:off
    http.csrf().disable()
        .authorizeRequests()
          /*.antMatchers("/", "/icons/**", "/welcome", "/signin", "/authentication", "/swagger-ui.html", "/swagger-resources/**",
            "/v2/**", "/webjars/**").permitAll()
          .antMatchers("/users", "/users/**", "/teams", "/teams/**", "/leavetypes", "/leavetypes/**", "/leaveprofiles",
            "/leaveprofiles/**, /requests/**").hasAuthority("HR_MANAGER")*/
          .anyRequest().permitAll()
        .and()
          .logout()
          .invalidateHttpSession(true)
          .deleteCookies("JSESSIONID")
          .logoutSuccessHandler(new CustomLogoutSuccessHandler())
          .logoutSuccessUrl("http://localhost:3000/")
          .permitAll()
        .and()
        .addFilterBefore(
          new AuthenticationFilter(authenticationManager(), tokenStore()),
          BasicAuthenticationFilter.class);
    //@formatter:on
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(new CustomAuthenticationProvider());
  }

  @Bean
  public TokenStore tokenStore() {
    return new TokenStore();
  }

  private class CustomAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
      return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
      return true;
    }
  }

  private class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
        throws IOException, ServletException {
      String token = request.getHeader(AUTHENTICATION_TOKEN_KEY);
      User user = tokenStore().getUser(token);
      tokenStore().removeTokensForUser(user);
    }
  }

}
