package com.studymate.apis.controller;

import com.studymate.apis.constansts.URLMappingConstants;
import com.studymate.apis.service.WebServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(URLMappingConstants.VERSION_PREFIX)
@RestController
public class WebServicesController {
    private static Logger log = LogManager.getLogger(WebServicesController.class);

    @Autowired
    private WebServices service;

    @PostMapping(URLMappingConstants.CREATE_USER)
    @ResponseBody
    public ResponseEntity<String> CreateUser(@RequestParam("username") String userName, @RequestParam("password") String password) {
        log.info("Creating user");
        return service.createUser(userName,password);
    }

}
