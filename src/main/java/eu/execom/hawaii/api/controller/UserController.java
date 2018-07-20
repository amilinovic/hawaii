package eu.execom.hawaii.api.controller;

import eu.execom.hawaii.dto.UserDto;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

  private static final ModelMapper MAPPER = new ModelMapper();

  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> getUsers(@RequestParam boolean active) {
    List<User> users = userService.findAllByActive(active);
    List<UserDto> userDtos = users.stream().map(UserDto::new).collect(Collectors.toList());
    return new ResponseEntity<>(userDtos, HttpStatus.OK);
  }

  @GetMapping("/{email}")
  public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
    User user = userService.findByEmail(email);
    UserDto userDto = new UserDto(user);
    return new ResponseEntity<>(userDto, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
    User user = MAPPER.map(userDto, User.class);
    user = userService.save(user);
    UserDto userDtoResponse = new UserDto(user);
    return new ResponseEntity<>(userDtoResponse, HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
    User user = MAPPER.map(userDto, User.class);
    user = userService.save(user);
    UserDto userDtoResponse = new UserDto(user);
    return new ResponseEntity<>(userDtoResponse, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity deleteUser(@PathVariable Long id) {
    userService.delete(id);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

}
