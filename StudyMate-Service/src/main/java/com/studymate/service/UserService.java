package com.studymate.service;

import com.studymate.dtos.GroupDto;
import com.studymate.dtos.NotificationDto;
import com.studymate.dtos.SessionDto;
import com.studymate.dtos.UserDto;
import com.studymate.model.Group;
import com.studymate.model.Notification;
import com.studymate.model.Session.Session;
import com.studymate.model.User;
import com.studymate.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
        responseInCaseOfUserNotFound(user);
        UserDto userDto = new UserDto(user.getUserName(), user.getEncryptedPassword(), user.getEmail(),
                user.getUniversity(), user.getDegree(), user.getCurriculum(), user.getGender(),user.getMemberSince());
        log.info("User retrieved successfully");
        return ResponseEntity.ok(userDto);
    }

    public ResponseEntity<List<GroupDto>> getUserGroups(String username) {
        log.info("Getting user groups");

        // Fetch user by username
        User user = userRepository.findByUserName(username);
        responseInCaseOfUserNotFound(user);

        // Get groups and handle null case
        List<Group> groups = user.getGroups();
        if (groups == null) {
            groups = new ArrayList<>(); // Initialize to an empty list to avoid NullPointerException
        }

        // Convert Group entities to GroupDto
        List<GroupDto> groupDto = new ArrayList<>();
        for (Group group : groups) {
            if (group != null) { // Additional null check in case there are null elements in the list
                groupDto.add(new GroupDto(
                        group.getGroupName(),
                        group.getInstitute(),
                        group.getCurriculum(),
                        group.getAdminsNames(),
                        group.getMembersNames()
                ));
            } else {
                log.warn("Encountered a null group in the user's group list");
            }
        }

        log.info("User groups retrieved successfully");
        return ResponseEntity.ok(groupDto);
    }


    public ResponseEntity<List<NotificationDto>> getUserNotifications(String username) {
        log.info("Getting user notifications");
        User user = userRepository.findByUserName(username);
        responseInCaseOfUserNotFound(user);
        List<Notification> notifications = user.getNotifications();
        List<NotificationDto> notificationDto = new ArrayList<>();
        for(Notification notification : notifications) {
            notificationDto.add(new NotificationDto(notification.getMessage(), notification.getUser().getUserName(),
                    notification.getUrl().toString(), notification.getCreatedDate().toString()));
        }
        log.info("User notifications retrieved successfully");
        return ResponseEntity.ok(notificationDto);
    }

        public ResponseEntity<List<SessionDto>> getUserSessions(String username) {
        log.info("Getting user sessions");
        User user = userRepository.findByUserName(username);
        if (responseInCaseOfUserNotFound(user).getStatusCode()==HttpStatus.OK)
        {
        List<Session> sessions = user.getSessions();
        List<SessionDto> sessionsDto = new ArrayList<>();
        for(Session session : sessions) {
            sessionsDto.add(new SessionDto(session.getParticipantsName(), session.getAdminsName(),
                   session.getCreationDate(), session.getSessionDate(),session.getLocation(),session.getDescription(),
                    session.isLimitParticipants(),session.getGroup().getGroupName(),session.getMaxParticipants(),
                    session.getSessionId()));
        }
        log.info("User sessions retrieved successfully");
        return ResponseEntity.ok(sessionsDto);
        }
        return ResponseEntity.badRequest().body(null);
    }


    // this function return ResponseEntity.ok in case of user found
    public static ResponseEntity<String> responseInCaseOfUserNotFound(User u)
    {
      if (u==null)
      {
          return ResponseEntity.badRequest().body(String.format("user is not found"));
      }
      return ResponseEntity.ok(null);
    }

    public boolean isInGroup(String username, String groupName) {
        log.info("Checking if user is in group");
        User user = userRepository.findByUserName(username);
        if(user == null) {
            log.error("User not found");
            return false;
        }
        for(Group group : user.getGroups()) {
            if(group.getGroupName().equals(groupName)) {
                log.info("User is in group");
                return true;
            }
        }
        log.info("User is not in group");
        return false;
    }
}