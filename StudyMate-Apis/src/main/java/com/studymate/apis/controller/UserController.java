package com.studymate.apis.controller;

import com.studymate.dtos.GroupDto;
import com.studymate.dtos.NotificationDto;
import com.studymate.dtos.UserDto;
import com.studymate.model.Notification;
import com.studymate.service.AuthenticationService;
import com.studymate.service.GroupService;
import com.studymate.service.UserService;
import com.studymate.apis.constansts.URLMappingConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${jwt.secret}")
    private String SECRET_KEY;

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
    public ResponseEntity<List<String>> GetAllUsers() {
        log.info("Getting all users");
        return userService.getAllUserNames();
    }

    @GetMapping(URLMappingConstants.GET_USER)
    @ResponseBody
    public ResponseEntity<UserDto> GetUser(@RequestHeader("Authorization") String token,@PathVariable String username) {
        if (authenticationService.validateToken(token,SECRET_KEY)) {
            return userService.getUser(username);
        } else {
            log.error("Unauthorized access");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping(URLMappingConstants.GET_USER_NOTIFICATIONS)
    @ResponseBody
    public ResponseEntity<List<NotificationDto>> GetUserNotifications(@PathVariable String username) {
        log.info("Getting user notifications");
        return userService.getUserNotifications(username);
    }

    @GetMapping(URLMappingConstants.GET_USER_GROUPS)
    @ResponseBody
    public ResponseEntity<List<GroupDto>> GetUserGroups(@PathVariable String username) {
        log.info("Getting user groups");
        return userService.getUserGroups(username);
    }


//    public ResponseEntity<String> CreateGroup(@RequestBody GroupDto groupDto) {//todo check amit created a new controller class for group
//        log.info("Creating new group");
//        return groupService.createGroup(groupDto.getGroupName(),groupDto.getUniversity(),
//                groupDto.getCurriculum(),groupDto.getGroupAdmin(),groupDto.getMembers());
//    @PostMapping(URLMappingConstants.CREATE_GROUP)
//    @ResponseBody
//    public ResponseEntity<String> CreateGroup(@RequestBody UserDto userDto) {
//        log.info("Creating new group");
//      //  return service.createUser(userName,password);
//        return userService.addUser(userDto.getUsername(), userDto.getPassword(), userDto.getEmail(), userDto.getUniversity(), userDto.getDegree(),
//                userDto.getCurriculum(), userDto.getGender());
//    }



}
