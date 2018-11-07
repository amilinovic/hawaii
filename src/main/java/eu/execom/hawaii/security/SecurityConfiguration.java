package eu.execom.hawaii.security;

import eu.execom.hawaii.service.IdTokenVerifier;
import eu.execom.hawaii.service.UserService;
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

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity @EnableSwagger2 public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    private final IdTokenVerifier idTokenVerifier;

    public SecurityConfiguration(IdTokenVerifier idTokenVerifier, UserService userService) {
        this.idTokenVerifier = idTokenVerifier;
        this.userService = userService;
    }

    @Override protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
        http.csrf().disable()
            .sessionManagement().sessionCreationPolicy(STATELESS)
            .and()
                .authorizeRequests()
                    .antMatchers("/api/users", "/api/users/**", "/api/teams", "/api/teams/**", "/api/leavetypes", "/api/leavetypes/**", "/api/leaveprofiles",
                        "/api/leaveprofiles/**", "/api/requests/**").hasAuthority("HR_MANAGER")
                    .antMatchers("/api/**").permitAll()
            .and()
                .addFilterBefore(idTokenVerifierFilter(), AnonymousAuthenticationFilter.class);
        //@formatter:on
    }

    @Override public void configure(WebSecurity web) throws Exception {
        web.ignoring()
           .antMatchers("/", "/icons/**", "/swagger-ui.html", "/swagger-resources/**", "/v2/**",
                   "/webjars/**");
    }

    @Override protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(new CustomAuthenticationProvider());
    }

    @Bean public IdTokenVerifierFilter idTokenVerifierFilter() throws Exception {
        return new IdTokenVerifierFilter(idTokenVerifier, userService, authenticationManagerBean());
    }

    private class CustomAuthenticationProvider implements AuthenticationProvider {
        @Override public Authentication authenticate(Authentication authentication) {
            return authentication;
        }

        @Override public boolean supports(Class<?> authentication) {
            return true;
        }
    }
}
