package eu.execom.hawaii.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import eu.execom.hawaii.service.GoogleTokenIdentityVerifier;
import eu.execom.hawaii.service.TokenIdentityVerifier;
import eu.execom.hawaii.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@EnableSwagger2
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
  private final List<String> clientIds;
  private final UserService userService;

  public SecurityConfiguration(@Value("#{'${security.google-client-ids}'.split(',')}") List<String> googleClientIds,
      UserService userService) {
    this.clientIds = googleClientIds;
    this.userService = userService;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //@formatter:off
      http.csrf().disable()
        .sessionManagement().sessionCreationPolicy(STATELESS)
          .and()
            .authorizeRequests()
              .antMatchers("/api/users/me","/api/users/image/**").permitAll()
              .antMatchers("/api/users", "/api/users/**", "/api/teams", "/api/teams/**", "/api/leavetypes", "/api/leavetypes/**", "/api/leaveprofiles",
                "/api/leaveprofiles/**", "/api/requests/**").hasAuthority("HR_MANAGER")
              .antMatchers("/api/**").permitAll()
          .and()
            .addFilterBefore(idTokenVerifierFilter(), AnonymousAuthenticationFilter.class);
    //@formatter:on
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring()
       .antMatchers("/", "/static/**", "/swagger-ui.html", "/swagger-resources/**", "/v2/**", "/webjars/**",
           "/**/*.{js,css}", "/favicon.ico", "/index.html", "/configuration/**", "/login");
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(new CustomAuthenticationProvider());
  }

  @Bean
  public IdTokenVerifierFilter idTokenVerifierFilter() throws Exception {
    return new IdTokenVerifierFilter(tokenIdentityVerifier(), userService, authenticationManagerBean());
  }

  @Bean
  public TokenIdentityVerifier tokenIdentityVerifier() {
    return new GoogleTokenIdentityVerifier(googleIdTokenVerifier());
  }

  @Bean
  public GoogleIdTokenVerifier googleIdTokenVerifier() {
    HttpTransport transport = new NetHttpTransport();
    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

    return new GoogleIdTokenVerifier.Builder(transport, jsonFactory).setAudience(clientIds).build();
  }

  private class CustomAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) {
      return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
      return true;
    }
  }
}
