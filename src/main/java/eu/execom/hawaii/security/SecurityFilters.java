package eu.execom.hawaii.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SecurityFilters {
    private final IdTokenVerifierFilter idTokenVerifierFilter;

    @Autowired
    public SecurityFilters(IdTokenVerifierFilter idTokenVerifierFilter) {
        this.idTokenVerifierFilter = idTokenVerifierFilter;
    }

    @Bean
    public FilterRegistrationBean idTokenVerifierFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(idTokenVerifierFilter);
        filterRegistrationBean.setUrlPatterns(Collections.singletonList("/api/*"));

        return filterRegistrationBean;
    }
}
