package eu.execom.hawaii.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.execom.hawaii.dto.UserDto;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

  private static final ModelMapper MAPPER = new ModelMapper();

  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<UserDto>> getUsers() {
    List<User> users = userService.getAll();
    List<UserDto> userDtos = users.stream().map(UserDto::new).collect(Collectors.toList());
    return new ResponseEntity<>(userDtos, HttpStatus.OK);
  }

  @GetMapping(value = "/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String email) {
    User user = userService.getByEmail(email);
    UserDto userDto = new UserDto(user);
    return new ResponseEntity<>(userDto, HttpStatus.OK);
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
    User user = MAPPER.map(userDto, User.class);
    user = userService.save(user);
    UserDto userDtoResponse = new UserDto(user);
    return new ResponseEntity<>(userDtoResponse, HttpStatus.CREATED);
  }

  @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
    User user = MAPPER.map(userDto, User.class);
    user = userService.save(user);
    UserDto userDtoResponse = new UserDto(user);
    return new ResponseEntity<>(userDtoResponse, HttpStatus.OK);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity deleteUser(@PathVariable("id") Long id) {
    userService.delete(id);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

}
