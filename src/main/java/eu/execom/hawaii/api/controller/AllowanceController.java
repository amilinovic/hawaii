package eu.execom.hawaii.api.controller;

import eu.execom.hawaii.dto.AllowanceForUserDto;
import eu.execom.hawaii.dto.AllowanceWithoutYearDto;
import eu.execom.hawaii.model.Allowance;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.service.AllowanceService;
import eu.execom.hawaii.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

@RestController
@RequestMapping("/api/allowances")
public class AllowanceController {

  private AllowanceService allowanceService;
  private UserService userService;

  private static final ModelMapper MAPPER = new ModelMapper();

  @Autowired
  public AllowanceController(AllowanceService allowanceService, UserService userService) {
    this.allowanceService = allowanceService;
    this.userService = userService;
  }

  @GetMapping("/years/range")
  public ResponseEntity<Map<String, Integer>> getFirstAndLastAllowancesYear(
      @ApiIgnore @AuthenticationPrincipal User authUser) {
    var firstAndLastAllowanceYear = allowanceService.getFirstAndLastAllowancesYear(authUser);
    return new ResponseEntity<>(firstAndLastAllowanceYear, HttpStatus.OK);
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<AllowanceForUserDto> getAllowancesForUser(@PathVariable Long id) {
    var user = userService.getUserById(id);
    AllowanceForUserDto allowanceForUserDto = allowanceService.getAllowancesForUser(user);

    return new ResponseEntity<>(allowanceForUserDto, HttpStatus.OK);
  }

  @GetMapping("/user/{id}/year")
  public ResponseEntity<AllowanceWithoutYearDto> getAllowanceForUserInYear(@PathVariable Long id,
      @RequestParam int year) {
    Allowance allowance = allowanceService.getByUserAndYear(id, year);
    return new ResponseEntity<>(new AllowanceWithoutYearDto(allowance), HttpStatus.OK);
  }

  @GetMapping("/me")
  public ResponseEntity<AllowanceWithoutYearDto> getAllowancesForAuthUser(@ApiIgnore @AuthenticationPrincipal User authUser,
      @RequestParam int year) {
    Allowance allowance = allowanceService.getByUserAndYear(authUser.getId(), year);
    return new ResponseEntity<>(new AllowanceWithoutYearDto(allowance), HttpStatus.OK);
  }
}
