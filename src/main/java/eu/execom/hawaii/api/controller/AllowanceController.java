package eu.execom.hawaii.api.controller;

import eu.execom.hawaii.service.AllowanceService;
import eu.execom.hawaii.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/allowances")
public class AllowanceController {

  private AllowanceService allowanceService;
  private UserService userService;

  @Autowired
  public AllowanceController(AllowanceService allowanceService, UserService userService) {
    this.allowanceService = allowanceService;
    this.userService = userService;
  }

  @GetMapping("/years/range/{id}")
  public ResponseEntity<Map<String, Integer>> getFirstAndLastAllowancesYear(@PathVariable("id") Long userId){
    var firstAndLastAllowanceYear = allowanceService.getFirstAndLastAllowancesYear(userId);
    return new ResponseEntity<>(firstAndLastAllowanceYear, HttpStatus.OK);
  }

}
