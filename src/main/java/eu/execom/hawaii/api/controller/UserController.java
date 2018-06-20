package eu.execom.hawaii.api.controller;

import eu.execom.hawaii.dto.UserDto;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

  private UserService userService;

  private static final ModelMapper mapper = new ModelMapper();

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PreAuthorize("hasRole('HR_MANAGER')")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<UserDto>> getUsers() {
    var users = userService.getAllUsers();
    var userDtos = users.stream().map(UserDto::new).collect(Collectors.toList());
    return new ResponseEntity<>(userDtos, HttpStatus.OK);
  }

  @GetMapping(value = "/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String email) {
    var user = userService.getUserByEmail(email);
    var userDto = new UserDto(user);
    return new ResponseEntity<>(userDto, HttpStatus.OK);
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
    var user = mapper.map(userDto, User.class);
    user = userService.saveUser(user);
    var userDtoResponse = new UserDto(user);
    return new ResponseEntity<>(userDtoResponse, HttpStatus.CREATED);
  }

  @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
    var user = mapper.map(userDto, User.class);
    user = userService.updateUser(user);
    var userDtoResponse = new UserDto(user);
    return new ResponseEntity<>(userDtoResponse, HttpStatus.OK);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity deleteUser(@PathVariable("id") Long id) {
    userService.deleteUser(id);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

}
