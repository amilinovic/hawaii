package eu.execom.hawaii.security;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
class SecurityTestController {
    @GetMapping("/test")
    ResponseEntity<String> testAction() {
        return ResponseEntity.ok("Security test action reached.");
    }
}
