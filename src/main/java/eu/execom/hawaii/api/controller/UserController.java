package eu.execom.hawaii.api.controller;

import eu.execom.hawaii.dto.UserDto;
import eu.execom.hawaii.dto.UserRestrictedDto;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.UserStatusType;
import eu.execom.hawaii.model.Team;
import eu.execom.hawaii.service.DayService;
import eu.execom.hawaii.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private static final ModelMapper MAPPER = new ModelMapper();

  private UserService userService;
  private DayService dayService;

  @Autowired
  public UserController(UserService userService, DayService dayService) {
    this.userService = userService;
    this.dayService = dayService;
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> getUsers(@RequestParam(required = false) List<UserStatusType> userStatusType) {
    List<User> users;
    if (userStatusType != null && !userStatusType.isEmpty()) {
      users = userService.findAllByUserStatusType(userStatusType);
    } else {
      users = userService.findAllUsers();
    }
    List<UserDto> userDtos = users.stream().map(UserDto::new).collect(Collectors.toList());

    return new ResponseEntity<>(userDtos, HttpStatus.OK);
  }

  @GetMapping("/restricted/all_users")
  public ResponseEntity<List<UserRestrictedDto>> allUsersRestrictedList(
      @RequestParam(required = false) LocalDate startTime, @RequestParam(required = false) LocalDate endTime) {

    Pair<LocalDate, LocalDate> newDates = assignDefaultDates(startTime, endTime);
    startTime = newDates.getLeft();
    endTime = newDates.getRight();

    List<UserStatusType> statuses = new ArrayList<UserStatusType>();
    statuses.add(UserStatusType.ACTIVE);
    List<User> users = userService.findAllByUserStatusType(statuses);

    List<UserRestrictedDto> userDtos = createUserRestrictedDtosFromUsers(users, startTime, endTime);

    return new ResponseEntity<>(userDtos, HttpStatus.OK);
  }

  @GetMapping("/restricted/team_users")
  public ResponseEntity<List<UserRestrictedDto>> teamUsersRestrictedList(
      @ApiIgnore @AuthenticationPrincipal User authUser, @RequestParam(required = false) LocalDate startTime,
      @RequestParam(required = false) LocalDate endTime) {

    Pair<LocalDate, LocalDate> newDates = assignDefaultDates(startTime, endTime);
    startTime = newDates.getLeft();
    endTime = newDates.getRight();

    Team team = authUser.getTeam();

    List<User> users = userService.findAllActiveUserByTeam(team);
    List<UserRestrictedDto> userDtos = createUserRestrictedDtosFromUsers(users, startTime, endTime);

    return new ResponseEntity<>(userDtos, HttpStatus.OK);
  }

  private Pair<LocalDate, LocalDate> assignDefaultDates(LocalDate startTime, LocalDate endTime) {
    if (startTime == null && endTime == null) {
      startTime = LocalDate.now().with(DayOfWeek.MONDAY);
      endTime = LocalDate.now().with(DayOfWeek.SUNDAY);
    } else if (startTime == null) {
      startTime = endTime.minusDays(7);
    } else {
      endTime = startTime.plusDays(7);
    }
    return new Pair<>(startTime, endTime);
  }

  private List<UserRestrictedDto> createUserRestrictedDtosFromUsers(List<User> users, LocalDate startTime,
      LocalDate endTime) {
    return users.stream()
                .map(u -> new UserRestrictedDto(u, this.dayService.getUserAbsencesDays(u, startTime, endTime)))
                .collect(Collectors.toList());
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
    user = userService.createAllowanceForUserOnCreateUser(user);
    return new ResponseEntity<>(new UserDto(user), HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
    User user = MAPPER.map(userDto, User.class);
    user = userService.save(user);

    return new ResponseEntity<>(new UserDto(user), HttpStatus.OK);
  }

  @GetMapping("/image/{id}")
  public ResponseEntity<byte[]> getUserImage(@PathVariable Long id) {
    User user = userService.getUserById(id);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.IMAGE_JPEG);
    headers.setContentLength(user.getImage().length);

    return new ResponseEntity<>(user.getImage(), headers, HttpStatus.OK);
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

  @GetMapping("/me")
  public ResponseEntity<UserDto> me(@ApiIgnore @AuthenticationPrincipal User authUser) {
    return new ResponseEntity<>(new UserDto(authUser), HttpStatus.OK);
  }

}
