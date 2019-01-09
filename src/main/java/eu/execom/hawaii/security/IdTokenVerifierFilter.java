package eu.execom.hawaii.security;

import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.UserStatusType;
import eu.execom.hawaii.service.TokenIdentityVerifier;
import eu.execom.hawaii.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Null;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Slf4j
public class IdTokenVerifierFilter extends OncePerRequestFilter {
  public static final String ID_TOKEN_HEADER = "X-ID-TOKEN";

  private final TokenIdentityVerifier tokenIdentityVerifier;
  private final UserService userService;
  private final AuthenticationManager authenticationManager;

  public IdTokenVerifierFilter(TokenIdentityVerifier tokenIdentityVerifier, UserService userService,
      AuthenticationManager authenticationManager) {

    this.tokenIdentityVerifier = tokenIdentityVerifier;
    this.userService = userService;
    this.authenticationManager = authenticationManager;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
      FilterChain filterChain) throws ServletException, IOException {
    String idToken = httpServletRequest.getHeader(ID_TOKEN_HEADER);

    if (idToken == null) {
      log.error("Id token not set in header for url path: {}", httpServletRequest.getServletPath());
    }

    Optional<String> userIdentity = getUserIdentity(idToken);

    if (!userIdentity.isPresent()) {
      httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
      log.error("Not established user identity from id token: {}", idToken);
      return;
    }

    User user = userService.findByEmail(userIdentity.get());

    if (user == null) {
      httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
      log.error("User {} not found in database", userIdentity.get());
      return;
    }

    user.getUserPushTokens();

    System.out.println("-------------");
    System.out.println(user);

    if (user.getUserStatusType()== null || !user.getUserStatusType().equals(UserStatusType.ACTIVE)) {
      httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
      log.error("User {} found in database, but is not active", userIdentity.get());
      return;
    }

    Authentication authentication = authenticationManager.authenticate(
        new PreAuthenticatedAuthenticationToken(user, null,
            Collections.singletonList(new SimpleGrantedAuthority(user.getUserRole().toString()))));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }

  private Optional<String> getUserIdentity(String token) {
    return token == null ? Optional.empty() : tokenIdentityVerifier.getIdentityOf(token);
  }
}
