package eu.execom.hawaii.api.controller;

import eu.execom.hawaii.service.AllowanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/allowances")
public class AllowanceController {

  private AllowanceService allowanceService;

  @Autowired
  public AllowanceController(AllowanceService allowanceService) {
    this.allowanceService = allowanceService;
  }

 /* @GetMapping("/years/range")
  public ResponseEntity<Map<String, Integer>> getFirstAndLastAllowancesYear(
      @ApiIgnore @AuthenticationPrincipal User authUser) {
    var firstAndLastAllowanceYear = allowanceService.getFirstAndLastAllowancesYear(authUser);
    return new ResponseEntity<>(firstAndLastAllowanceYear, HttpStatus.OK);
  }*/

}
