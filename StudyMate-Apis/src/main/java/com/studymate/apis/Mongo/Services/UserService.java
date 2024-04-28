package com.studymate.apis.Mongo.Services;

import Model.User;
import com.studymate.apis.Mongo.Repositories.UserRepository;
import com.studymate.apis.controller.WebServicesController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger log = LogManager.getLogger(WebServicesController.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<String> addUser(String username, String password, String email, String university, String degree, String curriculum, String gender) {
        log.info("Creating user");
        if(userRepository.findByUserName(username) != null) {
            String errorMsg = "Username already exists";
            log.error(errorMsg);
            return ResponseEntity.badRequest().body(errorMsg);
        }
        if(userRepository.findByEmail(email) != null) {
            String errorMsg = "Email already exists";
            log.error(errorMsg);
            return ResponseEntity.badRequest().body(errorMsg);
        }
        try {
            User user = new User(username, password, email, university, degree, curriculum,gender);
            userRepository.save(user);
            log.info("User created successfully");
            return ResponseEntity.ok("User created successfully");
        }
        catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}