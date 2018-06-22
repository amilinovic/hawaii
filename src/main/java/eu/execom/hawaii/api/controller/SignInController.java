package eu.execom.hawaii.api.controller;

import eu.execom.hawaii.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.security.Principal;

@RestController
public class SignInController {

  private UserService userService;

  //  private AuthorizationServerTokenServices authorizationServerTokenServices;
  //
  //  private ConsumerTokenServices consumerTokenServices;

  @Autowired
  public SignInController(UserService userService,
      AuthorizationServerTokenServices authorizationServerTokenServices, ConsumerTokenServices consumerTokenServices) {
    this.userService = userService;
    this.authorizationServerTokenServices = authorizationServerTokenServices;
    this.consumerTokenServices = consumerTokenServices;
  }

  @GetMapping(value = "/signin")
  public ResponseEntity<Principal> signIn(Principal principal) {
    //    OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
    //    Map<String, String> details = (Map<String, String>) oAuth2Authentication.getUserAuthentication().getDetails();
    //    String email = details.get("email");
    //    User user = userService.getByEmail(email);
    //
    //    final Collection<GrantedAuthority> authorities = new ArrayList<>();
    //    authorities.add(new SimpleGrantedAuthority(user.getUserRole().name()));
    //    //    oAuth2Authentication.getUserAuthentication().getAuthorities().addAll(authorities);
    //    //    ((OAuth2Authentication) principal).getUserAuthentication().getAuthorities().addAll(authorities);
    //
    //    final Authentication authentication = new PreAuthenticatedAuthenticationToken(principal, null, authorities);
    //    SecurityContextHolder.getContext().setAuthentication(authentication);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create("http://localhost:3000"));

    return new ResponseEntity<>(principal, headers, HttpStatus.FOUND);
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

  @GetMapping("/logout")
  public ResponseEntity logout(Principal principal) {

    OAuth2AccessToken token = authorizationServerTokenServices.getAccessToken((OAuth2Authentication) principal);
    consumerTokenServices.revokeToken(token.getValue());

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create("http://localhost:3000"));

    return new ResponseEntity<>(headers, HttpStatus.FOUND);
  }

}
