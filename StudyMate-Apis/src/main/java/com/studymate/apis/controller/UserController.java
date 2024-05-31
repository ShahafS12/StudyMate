package com.studymate.apis.controller;

import com.studymate.dtos.GroupDto;
import com.studymate.dtos.NotificationDto;
import com.studymate.dtos.UserDto;
import com.studymate.service.AuthenticationService;
import com.studymate.service.UserService;
import com.studymate.apis.constansts.URLMappingConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(URLMappingConstants.USERS)
@RestController
//@CrossOrigins(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController
{
    private static final Logger log = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;


    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(URLMappingConstants.CREATE_USER)
    @ResponseBody
    public ResponseEntity<String> CreateUser(@RequestBody UserDto userDto) {
        log.info("Creating user");
        return userService.addUser(userDto.getUsername(), userDto.getPassword(), userDto.getEmail(), userDto.getUniversity(), userDto.getDegree(),
                userDto.getCurriculum(), userDto.getGender());
    }

    @GetMapping(URLMappingConstants.ALL_USER_NAMES)
    @ResponseBody
    public ResponseEntity<List<String>> GetAllUsers(@RequestHeader("Authorization") String token) {
        if (authenticationService.validateToken(token)) {
            log.info("Getting all users");
            return userService.getAllUserNames();
        } else {
            log.error("Unauthorized access");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping(URLMappingConstants.GET_USER)
    @ResponseBody
    public ResponseEntity<UserDto> GetUser(@RequestHeader("Authorization") String token,@PathVariable String username) {
        if (authenticationService.validateToken(token)) {
            return userService.getUser(username);
        } else {
            log.error("Unauthorized access");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping(URLMappingConstants.GET_USER_NOTIFICATIONS)
    @ResponseBody
    public ResponseEntity<List<NotificationDto>> GetUserNotifications(@RequestHeader("Authorization") String token, @PathVariable String username) {
        if (authenticationService.validateToken(token)) {
            log.info("Getting user notifications");
            return userService.getUserNotifications(username);
        } else {
            log.error("Unauthorized access");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping(URLMappingConstants.GET_USER_GROUPS)
    @ResponseBody
    public ResponseEntity<List<GroupDto>> GetUserGroups(@RequestHeader("Authorization") String token, @PathVariable String username) {
        if (authenticationService.validateToken(token)) {
            log.info("Getting user groups");
            return userService.getUserGroups(username);
        } else {
            log.error("Unauthorized access");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

}
