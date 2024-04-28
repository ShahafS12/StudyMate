package com.studymate.apis.controller;

import com.studymate.apis.Mongo.Services.UserService;
import com.studymate.apis.constansts.URLMappingConstants;
import com.studymate.apis.service.WebServices;
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
    private WebServices service;
    @Autowired
    private UserService userService;

    @PostMapping(URLMappingConstants.CREATE_USER)
    @ResponseBody
    public ResponseEntity<String> CreateUser(@RequestParam("username") String userName, @RequestParam("password") String password
    ,@RequestParam("email") String email, @RequestParam("university") String university, @RequestParam("degree")
                                                 String degree, @RequestParam("type") String type,@RequestParam("gender")  String gender) {
        log.info("Creating user");
      //  return service.createUser(userName,password);
        return userService.addUser(userName,password,email,university,degree,type,gender);
    }



}
