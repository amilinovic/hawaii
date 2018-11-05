package eu.execom.hawaii.api.controller;

import eu.execom.hawaii.model.User;
import eu.execom.hawaii.service.AllowanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

@RestController
@RequestMapping("/api/allowances")
public class AllowanceController {

  private AllowanceService allowanceService;

  @Autowired
  public AllowanceController(AllowanceService allowanceService) {
    this.allowanceService = allowanceService;
  }

  @GetMapping("/years/range")
  public ResponseEntity<Map<String, Integer>> getFirstAndLastAllowancesYear(
      @ApiIgnore @AuthenticationPrincipal User authUser) {
    var firstAndLastAllowanceYear = allowanceService.getFirstAndLastAllowancesYear(authUser);
    return new ResponseEntity<>(firstAndLastAllowanceYear, HttpStatus.OK);
  }

}
