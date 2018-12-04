package eu.execom.hawaii.api.controller;

import eu.execom.hawaii.converter.UserStatusTypeConverter;
import eu.execom.hawaii.dto.UserDto;
import eu.execom.hawaii.model.Allowance;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.UserStatusType;
import eu.execom.hawaii.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private static final ModelMapper MAPPER = new ModelMapper();

  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @InitBinder
  public void initBinder(WebDataBinder webDataBinder) {
    webDataBinder.registerCustomEditor(UserStatusType.class, new UserStatusTypeConverter());
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> getUsers(@RequestParam(required = false) UserStatusType userStatusType) {
    List<User> users;
    if (userStatusType != null) {
      users = userService.findAllByUserStatusType(userStatusType);
    } else {
      users = userService.findAllUsers();
    }
    List<UserDto> userDtos = users.stream().map(UserDto::new).collect(Collectors.toList());

    return new ResponseEntity<>(userDtos, HttpStatus.OK);
  }

  @GetMapping("/search")
  public ResponseEntity<Page<UserDto>> searchUsersByNameAndEmail(@RequestParam int page, @RequestParam int size,
      @RequestParam UserStatusType userStatusType, @RequestParam String searchQuery) {
    Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "fullName");

    Page<User> users = userService.findAllByActiveAndEmailOrFullName(userStatusType, searchQuery, pageable);
    List<UserDto> userDtos = users.getContent().stream().map(UserDto::new).collect(Collectors.toList());
    Page<UserDto> pageableUserDtos = new PageImpl<>(userDtos, pageable, users.getTotalElements());

    return new ResponseEntity<>(pageableUserDtos, HttpStatus.OK);
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
    int latestYear = userAllowances.stream().map(Allowance::getYear).reduce((a, b) -> b).orElse(0);
    user = userService.createAllowanceForUser(user, latestYear + 1);

    return new ResponseEntity<>(new UserDto(user), HttpStatus.OK);
  }

  @PutMapping("/allowances/{year}")
  public ResponseEntity<List<UserDto>> createAllowanceForAllUsersForYear(@PathVariable int year) {
    List<User> users = userService.findAllByUserStatusType(UserStatusType.ACTIVE);

    List<UserDto> userDtos = users.stream()
                                  .map(user -> userService.createAllowanceForUser(user, year))
                                  .map(UserDto::new)
                                  .collect(Collectors.toList());

    return new ResponseEntity<>(userDtos, HttpStatus.OK);
  }

  @PutMapping("/{id}/activate")
  public ResponseEntity activateUser(@PathVariable Long id) {
    userService.activate(id);

    return new ResponseEntity(HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity deleteUser(@PathVariable Long id) {
    userService.delete(id);

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

}
