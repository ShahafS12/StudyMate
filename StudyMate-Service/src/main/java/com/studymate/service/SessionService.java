package com.studymate.service;

import com.studymate.dtos.GroupDto;
import com.studymate.dtos.NotificationDto;
import com.studymate.dtos.UserDto;
import com.studymate.model.Group;
import com.studymate.model.Notification;
import com.studymate.model.Session.Session;
import com.studymate.repositories.SessionRepository;
import com.studymate.repositories.UserRepository;
import com.studymate.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SessionService {
    private static final Logger log = LogManager.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public SessionService(UserRepository userRepository, SessionRepository sessionRepository) {

        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    public ResponseEntity<Session> getSession(UUID id) {
        try {
            Session session = sessionRepository.findBySessionId(id);
            if (session != null) {
                return ResponseEntity.ok(session);
            }
            log.error(String.format("Session %s not found", id));
            return ResponseEntity.badRequest().body(null);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }
}
