package com.studymate.apis.controller;

import com.studymate.apis.constansts.URLMappingConstants;
import com.studymate.apis.service.WebServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(URLMappingConstants.VERSION_PREFIX)
@RestController
public class WebServicesController {
    @Autowired
    private WebServices service;

    @PostMapping(URLMappingConstants.CREATE_USER)
    @ResponseBody
    public ResponseEntity<String> CreateUser(@RequestParam("username") String userName, @RequestParam("password") String password) {
        return service.createUser(userName,password);
    }

}
