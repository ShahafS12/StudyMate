package com.studymate.apis.service;

import Model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.logging.LogManager;

@Service
public class WebServices {
    public ResponseEntity<String> createUser(String userName, String password) {
        User user = new User(userName,password);
        return new ResponseEntity<>("user creation triggered for username: " + userName, HttpStatus.CREATED);
    }
}
