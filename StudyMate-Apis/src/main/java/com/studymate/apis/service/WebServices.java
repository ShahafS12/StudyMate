package com.studymate.apis.service;

import Model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.logging.LogManager;

@Service
public class WebServices {
    public ResponseEntity<String> createUser(String userName, String password) {
        try {
            User user = new User(userName,password);
            return new ResponseEntity<>(String.format("User %s created successfully", userName), HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(String.format("failed to create user %s with error %s", userName, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
