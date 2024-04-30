package com.studymate.apis.controller;

import com.studymate.apis.Mongo.Dtos.UserDto;
import com.studymate.apis.Mongo.Services.UserService;
import com.studymate.apis.constansts.URLMappingConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(URLMappingConstants.VERSION_PREFIX)
@RestController
//@CrossOrigins(origins = "http://localhost:3000", allowCredentials = "true")
public class WebServicesController {
    private static final Logger log = LogManager.getLogger(WebServicesController.class);

    @Autowired
    private UserService userService;

    @PostMapping(URLMappingConstants.CREATE_USER)
    @ResponseBody
    public ResponseEntity<String> CreateUser(@RequestBody UserDto userDto) {
        log.info("Creating user");
        return userService.addUser(userDto.getUsername(), userDto.getPassword(), userDto.getEmail(), userDto.getUniversity(), userDto.getDegree(),
                userDto.getCurriculum(), userDto.getGender());
    }

    @GetMapping(URLMappingConstants.LOGIN)
    @ResponseBody
    public ResponseEntity<String> Login(@RequestBody UserDto userDto) {
        log.info("Logging in user");
        return userService.loginUser(userDto.getUsername(), userDto.getPassword());
    }




}
