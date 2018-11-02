package eu.execom.hawaii.security;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
public class SecurityTestController {

    @GetMapping("/test")
    public ResponseEntity testAction(){
        return ResponseEntity.ok().build();
    }

}
