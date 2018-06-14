package eu.execom.hawaii.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.execom.hawaii.model.User;
import eu.execom.hawaii.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<List<User>> getUsers() {
    List<User> users = userService.getAllUsers();
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @GetMapping("/{email}")
  public ResponseEntity<User> getUserByEmail(@PathVariable("email") String email) {
    User user = userService.getUserByEmail(email);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }
}
