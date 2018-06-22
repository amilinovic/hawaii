package eu.execom.hawaii.api.controller;

import eu.execom.hawaii.model.User;
import eu.execom.hawaii.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RestController
public class SignInController {

  private UserService userService;

  @Autowired
  public SignInController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping(value = "/signin", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Principal> signIn(Principal principal) {
    OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
    Map<String, String> details = (Map<String, String>) oAuth2Authentication.getUserAuthentication().getDetails();
    String email = details.get("email");
    User user = userService.getByEmail(email);

    final Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(user.getUserRole().name()));
    //    oAuth2Authentication.getUserAuthentication().getAuthorities().addAll(authorities);
    //    ((OAuth2Authentication) principal).getUserAuthentication().getAuthorities().addAll(authorities);

    final Authentication authentication = new PreAuthenticatedAuthenticationToken(principal, null, authorities);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create("localhost:3000"));

    return new ResponseEntity<>(principal, headers, HttpStatus.FOUND);
  }

}
