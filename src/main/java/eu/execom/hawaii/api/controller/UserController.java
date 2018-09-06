package eu.execom.hawaii.api.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @GetMapping
  public ResponseEntity<List<UserDto>> getUsers(@RequestParam boolean active) {
    List<User> users = userService.findAllByActive(active);
    List<UserDto> userDtos = users.stream().map(UserDto::new).collect(Collectors.toList());

    return new ResponseEntity<>(userDtos, HttpStatus.OK);
  }

  @GetMapping("/{email}")
  public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
    User user = userService.findByEmail(email);

    return new ResponseEntity<>(new UserDto(user), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
    User user = MAPPER.map(userDto, User.class);
    user = userService.createAllowanceForUser(user, LocalDate.now().getYear());
    user = userService.save(user);

    return new ResponseEntity<>(new UserDto(user), HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
    User user = MAPPER.map(userDto, User.class);
    user = userService.save(user);

    return new ResponseEntity<>(new UserDto(user), HttpStatus.OK);
  }

  @PutMapping("/{userId}/allowance")
  public ResponseEntity<UserDto> createAllowanceForUserForNextYear(@PathVariable Long userId) {
    User user = userService.getUserById(userId);
    var userAllowances = user.getAllowances();
    var latestUserAllowance = userAllowances.get(userAllowances.size() - 1);
    var latestYear = latestUserAllowance.getYear();
    user = userService.createAllowanceForUser(user, latestYear + 1);
    user = userService.save(user);

    return new ResponseEntity<>(new UserDto(user), HttpStatus.OK);
  }

  @PutMapping("/allowances/{year}")
  public ResponseEntity<List<UserDto>> createAllowanceForUsersForNextYear(@PathVariable int year) {
    List<User> users = userService.findAllByActive(true);
    List<User> usersAllowances = users.stream()
                                      .map(user -> userService.createAllowanceForUser(user, year))
                                      .collect(Collectors.toList());

    List<UserDto> userDtos = userService.saveAll(usersAllowances)
                                        .stream()
                                        .map(UserDto::new)
                                        .collect(Collectors.toList());

    return new ResponseEntity<>(userDtos, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity deleteUser(@PathVariable Long id) {
    userService.delete(id);

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

}
