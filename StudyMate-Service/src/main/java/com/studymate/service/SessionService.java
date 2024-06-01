package com.studymate.service;

import com.studymate.dtos.SessionDto;
import com.studymate.model.Session.Session;
import com.studymate.repositories.SessionRepository;
import com.studymate.repositories.UserRepository;
import com.studymate.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    public ResponseEntity<SessionDto> getSession(String id) {
        try {
            Session session = sessionRepository.findBySessionId(UUID.fromString(id));
            SessionDto sessionDto= new SessionDto(session.getParticipantsName(),
                    session.getAdminsName(),session.getCreationDate(),session.getSessionDate(),
                    session.getLocation(),session.getSessionId(),session.getDescription());
            if (session != null) {
                return ResponseEntity.ok(sessionDto);
            }
        }
        catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
        return null;
    }
    public ResponseEntity<String> AdminAddUserToSession(String id, String adminStr, String usernameStr) {
        try {
            Session session = sessionRepository.findBySessionId(UUID.fromString(id));
            User admin=userRepository.findByUserName(adminStr);
            User user=userRepository.findByUserName(usernameStr);
            session.AdminAddParticipant(admin,user);
            sessionRepository.save(session);
            return ResponseEntity.ok("user added");
        }
        catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }
    public ResponseEntity<String> UserAddHimselfToSession(String id,String usernameStr) {
        try {
            Session session = sessionRepository.findBySessionId(UUID.fromString(id));
            User user=userRepository.findByUserName(usernameStr);
            session.addParticipant(user);
            sessionRepository.save(session);
            return ResponseEntity.ok("user added");
        }
        catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }
    public ResponseEntity<String> AdminRemoveUserFromSession(String id, String adminStr, String usernameStr) {
        try {
            Session session = sessionRepository.findBySessionId(UUID.fromString(id));
            User admin=userRepository.findByUserName(adminStr);
            User user=userRepository.findByUserName(usernameStr);
            session.removeParticipantByAdmin(admin,user);
            sessionRepository.save(session);
            return ResponseEntity.ok("user removed");
        }
        catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }
    public ResponseEntity<String> setUserAsAdminOfSession(String id,String adminStr,String usernameStr) {
        try {
            Session session = sessionRepository.findBySessionId(UUID.fromString(id));
            User admin=userRepository.findByUserName(adminStr);
            User user=userRepository.findByUserName(usernameStr);
            session.setParticipantAsAdmin(admin,user);
            sessionRepository.save(session);
            return ResponseEntity.ok("user is now admin");
        }
        catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }
    public ResponseEntity<String> removeUserFromAdminOfSession(String id,String adminStr,String usernameStr) {
        try {
            Session session = sessionRepository.findBySessionId(UUID.fromString(id));
            User admin=userRepository.findByUserName(adminStr);
            User user=userRepository.findByUserName(usernameStr);
            session.adminRemoveParticipantFromBeingAdmin(admin,user);
            sessionRepository.save(session);
            return ResponseEntity.ok("user is no longer admin admin");
        }
        catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }
    public ResponseEntity<String> deleteSessionByAdmin(String id,String adminStr) {
        try {
            Session session = sessionRepository.findBySessionId(UUID.fromString(id));
            User admin=userRepository.findByUserName(adminStr);
            session.cancelMeetingByAdmin(admin);
            sessionRepository.save(session);
            return ResponseEntity.ok("session is no longer available");
        }
        catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }
    public ResponseEntity<String> setMaxParticipants(String id, String adminStr, String isLimited, String maxParticipantsStr) {
        try {
            Session session = sessionRepository.findBySessionId(UUID.fromString(id));
            User admin=userRepository.findByUserName(adminStr);
            boolean limit=Boolean.getBoolean(isLimited);
            int maxParticipants= Integer.getInteger(maxParticipantsStr);
            session.setLimitParticipants(admin,maxParticipants,limit);
            sessionRepository.save(session);
            return ResponseEntity.ok("session is no longer available");
        }
        catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }



}
