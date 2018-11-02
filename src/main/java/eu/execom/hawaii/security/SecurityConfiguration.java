package eu.execom.hawaii.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableWebSecurity
@EnableSwagger2
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/icons/**", "/swagger-ui.html", "/swagger-resources/**").permitAll()
                .antMatchers("/users", "/users/**", "/teams", "/teams/**", "/leavetypes", "/leavetypes/**", "/leaveprofiles",
                        "/leaveprofiles/**, /requests/**").hasAuthority("HR_MANAGER")
                .anyRequest().permitAll();
        //@formatter:on
    }
}
