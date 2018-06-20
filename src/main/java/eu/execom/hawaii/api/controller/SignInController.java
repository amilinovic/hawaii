package eu.execom.hawaii.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class SignInController {

  @GetMapping(value = "/signin", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Principal> signIn(Principal principal) {
    return new ResponseEntity<>(principal, HttpStatus.OK);
  }

}
