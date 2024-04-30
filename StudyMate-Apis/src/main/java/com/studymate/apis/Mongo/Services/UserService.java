package com.studymate.apis.Mongo.Services;

import Model.User;
import com.studymate.apis.Mongo.Repositories.UserRepository;
import com.studymate.apis.controller.WebServicesController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;

@Service
public class UserService {
    private static final Logger log = LogManager.getLogger(WebServicesController.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
            String encodedPassword = passwordEncoder.encode(password);
            User user = new User(username, encodedPassword, email, university, degree, curriculum,gender);
            userRepository.save(user);
            log.info("User created successfully");
            return ResponseEntity.ok("User created successfully");
        }
        catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<String> loginUser(String username, String password) {
        log.info("Logging in user");
        User user = userRepository.findByUserName(username);
        if(user == null) {
            String errorMsg = "User not found";
            log.error(errorMsg);
            return ResponseEntity.badRequest().body(errorMsg);
        }
        if(passwordEncoder.matches(password, user.getEncryptedPassword())) {
            String token = Jwts.builder().subject(username).claim("roles", "user").compact();
            log.info("User logged in successfully");
            return ResponseEntity.ok(token);
        }
        else {
            String errorMsg = "Incorrect password";
            log.error(errorMsg);
            return ResponseEntity.badRequest().body(errorMsg);
        }
    }
}