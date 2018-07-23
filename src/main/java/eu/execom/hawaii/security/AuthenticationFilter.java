package eu.execom.hawaii.security;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

import eu.execom.hawaii.model.User;

public class AuthenticationFilter extends GenericFilterBean {

  public static final String AUTHENTICATION_TOKEN_KEY = "X-AUTH-TOKEN";

  private AuthenticationManager authenticationManager;
  private TokenStore tokenStore;

  public AuthenticationFilter(AuthenticationManager authenticationManager, TokenStore tokenStore) {
    this.authenticationManager = authenticationManager;
    this.tokenStore = tokenStore;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;

    Optional<String> token = Optional.ofNullable(httpRequest.getHeader(AUTHENTICATION_TOKEN_KEY));

    if (token.isPresent()) {
      Authentication authentication = authenticateWithToken(token.get());
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    chain.doFilter(request, response);
  }

  private Authentication authenticateWithToken(String token) {
    User user = tokenStore.getUser(token);

    if (user == null) {
      return null;
    }

    String userRole = user.getUserRole().name();
    SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userRole);
    List<GrantedAuthority> authorities = Collections.singletonList(grantedAuthority);

    PreAuthenticatedAuthenticationToken requestAuthentication = new PreAuthenticatedAuthenticationToken(user, token,
        authorities);
    return authenticationManager.authenticate(requestAuthentication);
  }
}