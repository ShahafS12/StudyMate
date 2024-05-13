package com.studymate.service;

import com.studymate.dtos.GroupDto;
import com.studymate.dtos.NotificationDto;
import com.studymate.dtos.UserDto;
import com.studymate.model.Group;
import com.studymate.model.Notification;
import com.studymate.repositories.UserRepository;
import com.studymate.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private static final Logger log = LogManager.getLogger(UserService.class);

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



    public ResponseEntity<List<String>> getAllUserNames()
    {
        log.info("Getting all users");
        try {
            List<String> userNames = userRepository.findAllUserNames();
            log.info("All users retrieved successfully");
            return ResponseEntity.ok(userNames);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<UserDto> getUser(String username) {
        log.info("Getting user");
        User user = userRepository.findByUserName(username);
        if(user == null) {
            log.error(String.format("User %s not found", username));
            return ResponseEntity.badRequest().body(null);
        }
        UserDto userDto = new UserDto(user.getUserName(), user.getEncryptedPassword(), user.getEmail(),
                user.getUniversity(), user.getDegree(), user.getCurriculum(), user.getGender(),user.getMemberSince());
        log.info("User retrieved successfully");
        return ResponseEntity.ok(userDto);
    }

    public ResponseEntity<List<GroupDto>> getUserGroups(String username) {
        log.info("Getting user groups");
        User user = userRepository.findByUserName(username);
        if(user == null) {
            log.error(String.format("User %s not found", username));
            return ResponseEntity.badRequest().body(null);
        }
        List<Group> groups = user.getGroups();
        List<GroupDto> groupDtos = new ArrayList<>();
        for(Group group : groups) {
            groupDtos.add(new GroupDto(group.getGroupName(), group.getInstitute(), group.getCurriculum(),
                    group.getGroupAdmin().getUserName(), group.getMembersNames()));
        }
        log.info("User groups retrieved successfully");
        return ResponseEntity.ok(groupDtos);
    }

    public ResponseEntity<List<NotificationDto>> getUserNotifications(String username) {
        log.info("Getting user notifications");
        User user = userRepository.findByUserName(username);
        if(user == null) {
            log.error(String.format("User %s not found", username));
            return ResponseEntity.badRequest().body(null);
        }
        List<Notification> notifications = user.getNotifications();
        List<NotificationDto> notificationDtos = new ArrayList<>();
        for(Notification notification : notifications) {
            notificationDtos.add(new NotificationDto(notification.getMessage(), notification.getUser().getUserName(),
                    notification.getUrl().toString(), notification.getCreatedDate().toString()));
        }
        log.info("User notifications retrieved successfully");
        return ResponseEntity.ok(notificationDtos);
    }
}