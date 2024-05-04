package com.studymate.apis.controller;

import com.studymate.apis.Mongo.Dtos.GroupDto;
import com.studymate.apis.Mongo.Dtos.UserDto;
import com.studymate.apis.Mongo.Services.GroupService;
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
    @Autowired
    private GroupService groupService;

    @PostMapping(URLMappingConstants.CREATE_USER)
    @ResponseBody
    public ResponseEntity<String> CreateUser(@RequestBody UserDto userDto) {
        log.info("Creating user");
        return userService.addUser(userDto.getUsername(), userDto.getPassword(), userDto.getEmail(), userDto.getUniversity(), userDto.getDegree(),
                userDto.getCurriculum(), userDto.getGender());
    }

    @PostMapping(URLMappingConstants.LOGIN)
    @ResponseBody

    public ResponseEntity<String> CreateGroup(@RequestBody GroupDto groupDto) {
        log.info("Creating new group");
        return groupService.createGroup(groupDto.getGroupName(), groupDto.getUniversity(),
                groupDto.getCurriculum(), groupDto.getGroupAdmin(), groupDto.getMembers());
    }

        public ResponseEntity<String> Login (@RequestBody UserDto userDto){
            log.info("Logging in user");
            return userService.loginUser(userDto.getUsername(), userDto.getPassword());

        }


    }

