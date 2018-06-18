package eu.execom.hawaii.api.controller;

import eu.execom.hawaii.dto.UserDto;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

  private UserService userService;

  public static final ModelMapper mapper = new ModelMapper();

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<UserDto>> getUsers() {
    List<User> users = userService.getAllUsers();
    List<UserDto> userDtos = users.stream().map(UserDto::new).collect(Collectors.toList());
    return new ResponseEntity<>(userDtos, HttpStatus.OK);
  }

  @GetMapping(value = "/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String email) {
    User user = userService.getUserDtoByEmail(email);
    UserDto userDto = new UserDto(user);
    return new ResponseEntity<>(userDto, HttpStatus.OK);
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
    User user = mapper.map(userDto, User.class);
    user = userService.saveUser(user);
    UserDto userDtoResponse = new UserDto(user);
    return new ResponseEntity<>(userDtoResponse, HttpStatus.CREATED);
  }

}
