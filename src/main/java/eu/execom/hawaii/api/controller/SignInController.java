package eu.execom.hawaii.api.controller;

import eu.execom.hawaii.dto.CreateTokenDto;
import eu.execom.hawaii.dto.UserPushTokenDto;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.UserPushToken;
import eu.execom.hawaii.repository.UserPushTokensRepository;
import eu.execom.hawaii.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api")
public class SignInController {

  private static final ModelMapper MAPPER = new ModelMapper();

  private UserService userService;
  private UserPushTokensRepository userPushTokensRepository;

  @Autowired
  public SignInController(UserService userService, UserPushTokensRepository userPushTokensRepository) {
    this.userService = userService;
    this.userPushTokensRepository = userPushTokensRepository;
  }

  @PostMapping("/token")
  public ResponseEntity<UserPushTokenDto> createToken(@ApiIgnore @AuthenticationPrincipal User authUser,
      @RequestBody CreateTokenDto createTokenDto) {
    UserPushToken userPushToken = userService.createUserToken(authUser, createTokenDto);
    return new ResponseEntity<>(new UserPushTokenDto(userPushToken), HttpStatus.OK);
  }

  @DeleteMapping("/token/{id}")
  public ResponseEntity deleteToken(@ApiIgnore @AuthenticationPrincipal User authUser, @PathVariable Long id) {
    userService.deleteUserPushToken(authUser, id);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

}