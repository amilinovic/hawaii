package eu.execom.hawaii.api.controller;

import eu.execom.hawaii.dto.CreateTokenDto;
import eu.execom.hawaii.model.GenericResponse;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api")
public class SignInController {

  private UserService userService;

  @Autowired
  public SignInController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/token")
  public ResponseEntity<GenericResponse> createToken(@ApiIgnore @AuthenticationPrincipal User authUser,
      @RequestBody CreateTokenDto createTokenDto) {
    userService.createUserToken(authUser, createTokenDto);
    return new ResponseEntity<>(GenericResponse.OK, HttpStatus.OK);
  }

  /*@PutMapping("/token")
  public ResponseEntity<GenericResponse> tokenReset(@ApiIgnore @AuthenticationPrincipal User authUser,
      @RequestParam String pushToken) {
    userService.updateUserPushToken(pushToken, authUser);
    return new ResponseEntity<>(GenericResponse.OK, HttpStatus.OK);
  }*/

}