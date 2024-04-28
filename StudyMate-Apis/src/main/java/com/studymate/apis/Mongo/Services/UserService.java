package com.studymate.apis.Mongo.Services;

import com.studymate.apis.Mongo.Document.UserDoc;
import com.studymate.apis.Mongo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<String> addUser(String username, String password, String email, String university, String degree, String type, String gender) {
        UserDoc newUser = new UserDoc();

        try {
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setEmail(email);
            newUser.setUniversity(university);
            newUser.setDegree(degree);
            newUser.setType(type);
            newUser.setGender(gender);

            userRepository.save(newUser);
            return ResponseEntity.status(201).body(String.format("User %s created successfully", username));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(500).body(String.format("failed to create user %s with error %s", username, e.getMessage()));
        }
    }
}