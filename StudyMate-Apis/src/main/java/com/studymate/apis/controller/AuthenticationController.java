package com.studymate.apis.controller;

import com.studymate.apis.constansts.URLMappingConstants;
import com.studymate.dtos.UserDto;
import com.studymate.service.AuthenticationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import static com.studymate.apis.constansts.URLMappingConstants.AUTHENTICATE;
import static com.studymate.apis.constansts.URLMappingConstants.VALIDATE_TOKEN;

@RequestMapping(AUTHENTICATE)
@RestController
public class AuthenticationController {

    private static final Logger log = LogManager.getLogger(AuthenticationController.class);
    private long EXPIRATION = 900000000 ;
    private String SECRET_KEY = "044561dc31ea927d424cf34c400e6c2433e6488aaa797f37a8a2a33c50303d91";

    @Autowired
    private AuthenticationService authenticationService;


    @PostMapping(URLMappingConstants.LOGIN)
    @ResponseBody
    public ResponseEntity<String> Login(@RequestBody UserDto userDto) {
        log.info("Logging in user");
        return authenticationService.loginUser(userDto.getUsername(), userDto.getPassword(),SECRET_KEY,EXPIRATION);

    }

    @GetMapping(VALIDATE_TOKEN)
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String token) {
        if (authenticationService.validateToken(token,SECRET_KEY)) {
            return ResponseEntity.ok("Valid token");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

}
