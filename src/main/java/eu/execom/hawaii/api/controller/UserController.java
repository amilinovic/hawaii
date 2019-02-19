package eu.execom.hawaii.api.controller;

import eu.execom.hawaii.dto.DayDto;
import eu.execom.hawaii.dto.UserDto;
import eu.execom.hawaii.dto.UserWithDaysDto;
import eu.execom.hawaii.model.Day;
import eu.execom.hawaii.model.Team;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.UserStatusType;
import eu.execom.hawaii.service.DayService;
import eu.execom.hawaii.service.TeamService;
import eu.execom.hawaii.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

  private static final ModelMapper MAPPER = new ModelMapper();

  private UserService userService;
  private DayService dayService;
  private TeamService teamService;

  @Autowired
  public UserController(UserService userService, DayService dayService, TeamService teamService) {
    this.userService = userService;
    this.dayService = dayService;
    this.teamService = teamService;
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

  @GetMapping("/allUsers")
  public ResponseEntity<List<UserWithDaysDto>> allUsersRestrictedList(
      @RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate) {
    startDate = assignDefaultStartDate(startDate, endDate);
    endDate = assignDefaultEndDate(startDate, endDate);
    List<UserStatusType> statuses = Collections.singletonList(UserStatusType.ACTIVE);
    List<User> users = userService.findAllByUserStatusType(statuses);
    List<UserWithDaysDto> userDtos = createUserRestrictedDtosFromUsers(users, startDate, endDate);

    return new ResponseEntity<>(userDtos, HttpStatus.OK);
  }

  @GetMapping("/teamUsers")
  public ResponseEntity<List<UserWithDaysDto>> teamUsersRestrictedList(
      @ApiIgnore @AuthenticationPrincipal User authUser, @RequestParam(required = false) LocalDate startDate,
      @RequestParam(required = false) LocalDate endDate, @RequestParam(required = false) Long teamId) {
    startDate = assignDefaultStartDate(startDate, endDate);
    endDate = assignDefaultEndDate(startDate, endDate);
    Team team;
    if (teamId != null) {
      team = teamService.getById(teamId);
      if (!authUser.getApproverTeams().contains(team) && !authUser.getTeam().equals(team)) {
        log.debug("User: '{}' is neither a member of this team nor a team approver for the team '{}' ",
            authUser.getEmail(), team.getName());
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
      }
    } else {
      team = authUser.getTeam();
    }

    List<User> users = userService.findAllActiveUsersByTeam(team);
    List<UserWithDaysDto> userDtos = createUserRestrictedDtosFromUsers(users, startDate, endDate);

    return new ResponseEntity<>(userDtos, HttpStatus.OK);
  }

  @GetMapping("/myDays")
  public ResponseEntity<List<DayDto>> myDays(@ApiIgnore @AuthenticationPrincipal User authUser,
      @RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate) {
    startDate = assignDefaultStartDate(startDate, endDate);
    endDate = assignDefaultEndDate(startDate, endDate);
    List<Day> days = dayService.getUserAbsencesDays(authUser, startDate, endDate);
    List<DayDto> daysDto = days.stream().map(DayDto::new).collect(Collectors.toList());

    return new ResponseEntity<>(daysDto, HttpStatus.OK);
  }

  private LocalDate assignDefaultStartDate(LocalDate startDate, LocalDate endDate) {
    if (startDate == null && endDate == null) {
      startDate = LocalDate.of(LocalDate.now().getYear(), 1, 1);
    } else if (startDate == null) {
      startDate = endDate.minusYears(1);
    }

    return startDate;
  }

  private LocalDate assignDefaultEndDate(LocalDate startDate, LocalDate endDate) {
    if (startDate == null && endDate == null) {
      endDate = LocalDate.of(LocalDate.now().getYear(), 12, 31);
    } else if (endDate == null) {
      endDate = startDate.plusYears(1);
    }

    return endDate;
  }

  private List<UserWithDaysDto> createUserRestrictedDtosFromUsers(List<User> users, LocalDate startDate,
      LocalDate endDate) {
    return users.stream()
                .map(u -> new UserWithDaysDto(u, dayService.getUserAbsencesDays(u, startDate, endDate)))
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
  public ResponseEntity<UserDto> createUser(@ApiIgnore @AuthenticationPrincipal User authUser,
      @RequestBody UserDto userDto) {
    User user = MAPPER.map(userDto, User.class);
    user = userService.createAllowanceForUserOnCreateUser(user, authUser);

    return new ResponseEntity<>(new UserDto(user), HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<UserDto> updateUser(@ApiIgnore @AuthenticationPrincipal User authUser,
      @RequestBody UserDto userDto) {
    var user = MAPPER.map(userDto, User.class);
    user = userService.update(user, authUser);

    return new ResponseEntity<>(new UserDto(user), HttpStatus.OK);
  }

  @PutMapping("/{id}/activate")
  public ResponseEntity activateUser(@ApiIgnore @AuthenticationPrincipal User authUser, @PathVariable Long id) {
    userService.activate(id, authUser);

    return new ResponseEntity(HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity deleteUser(@ApiIgnore @AuthenticationPrincipal User authUser, @PathVariable Long id) {
    userService.delete(id, authUser);

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/image/{id}")
  public ResponseEntity<byte[]> getUserImage(@PathVariable Long id) {
    User user = userService.getUserById(id);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.IMAGE_JPEG);
    headers.setContentLength(user.getImage().length);

    return new ResponseEntity<>(user.getImage(), headers, HttpStatus.OK);
  }

  @GetMapping("/me")
  public ResponseEntity<UserDto> me(@ApiIgnore @AuthenticationPrincipal User authUser) {
    return new ResponseEntity<>(new UserDto(authUser), HttpStatus.OK);
  }

}
