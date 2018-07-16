package eu.execom.hawaii.api.controller;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.google.api.plus.Person;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import eu.execom.hawaii.model.User;
import eu.execom.hawaii.service.UserService;

@RestController
public class SignInController {

  private static final String EXECOM_DOMAIN = "execom.eu";

  private UserService userService;

  @Autowired
  public SignInController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping(value = "/signin")
  public ResponseEntity<Principal> signIn(@RequestHeader(value = "Authorization") String token) {

    Google google = new GoogleTemplate(token);
    Person profile = google.plusOperations().getGoogleProfile();
    String email = profile.getAccountEmail();

    if (!email.endsWith(EXECOM_DOMAIN)) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    User user = userService.findByEmail(email);

    Collection<GrantedAuthority> authorities = Collections.singletonList(
        new SimpleGrantedAuthority(user.getUserRole().name()));

    Authentication authentication = new PreAuthenticatedAuthenticationToken(user, null, authorities);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/authentication")
  public ResponseEntity authenticate(HttpServletRequest request) {
    HttpStatus status = authenticationStatus(request);
    return new ResponseEntity(status);
  }

  private HttpStatus authenticationStatus(HttpServletRequest request) {
    Object authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication instanceof AnonymousAuthenticationToken) {
      return HttpStatus.UNAUTHORIZED;
    }
    return HttpStatus.OK;
  }

}
