package com.studymate.service;

import com.studymate.dtos.SessionDto;
import com.studymate.model.Group;
import com.studymate.model.Session.Session;
import com.studymate.repositories.GroupRepository;
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

import java.text.DateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SessionService {
    private static final Logger log = LogManager.getLogger(UserService.class);
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public SessionService(UserRepository userRepository, SessionRepository sessionRepository, GroupRepository groupRepository) {
        this.groupRepository=groupRepository;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    public ResponseEntity<String> createSession(String usernameStr, SessionDto sessionDto ) throws IllegalArgumentException {
        ResponseEntity<String> response;
        try {
            User creator  = userRepository.findByUserName(usernameStr);
            response=UserService.responseInCaseOfUserNotFound(creator);
            if (response.getStatusCode()==HttpStatus.OK){
                Group group = groupRepository.findByGroupName(sessionDto.getGroupName());
                response=GroupService.responseInCaseOfGroupNotFoundOrDeleted(group);
                if (response.getStatusCode()==HttpStatus.OK){
                    List<User> participants= new ArrayList<>();
                    for (String userStr: sessionDto.getMembersName())
                    {
                        if (response.getStatusCode()==HttpStatus.OK){
                            User user= userRepository.findByUserName(userStr);
                            if (user!=null)
                            {
                                participants.add(user);
                            }
                            else
                            {
                            response=ResponseEntity.badRequest().body(String.format("could not find user %s, could not create group",userStr));
                            }
                        }

                    }
                     if (response.getStatusCode()==HttpStatus.OK){

                        Session session= new Session(sessionDto.getSessionDate(),sessionDto.getLocation(),
                            participants,sessionDto.getMaxParticipants(),creator,sessionDto.isLimited(),
                            sessionDto.getDescription(),group);
                        if (session != null) {
                            for (User user:session.getParticipants())
                            {
                                userRepository.save(user);
                            }
                            groupRepository.save(group);
                            sessionRepository.save(session);
                            response= ResponseEntity.ok(String.format("session %s created", session.getSessionId()));
                        }
                     }
                }
            }
        }
        catch (IllegalArgumentException e) {
            response= ResponseEntity.badRequest().body(e.getMessage());
        }
        return response;
    }
    public ResponseEntity<SessionDto> getSession(String SessionId) {
        ResponseEntity<SessionDto> res = null;
        SessionDto sessionDto;
        try {
            Session session = sessionRepository.findBySessionId((SessionId));
            if (session!= null && ! session.isCanceled())
            {
                sessionDto= new SessionDto(session.getParticipantsName(),
                    session.getAdminsName(),session.getCreationDate(),session.getSessionDate(),
                    session.getLocation(),session.getDescription(),session.isLimitParticipants(),session.getGroup().getGroupName(),session.getMaxParticipants(),session.getSessionId());
                res=ResponseEntity.ok(sessionDto);
            }
        }
        catch (IllegalArgumentException e) {
            res= ResponseEntity.badRequest().body(null);
        }
        return res;
    }
    public ResponseEntity<String> AdminAddUserToSession(String id, String adminStr, String usernameStr) throws NullPointerException{
        ResponseEntity<String> response;
        try {
            Session session = sessionRepository.findBySessionId(id);
            response = responseInCaseOfNotFoundOrDeletedOrExpired(session);
            if (response.getStatusCode() == HttpStatus.OK) {
                User admin = userRepository.findByUserName(adminStr);
                response = UserService.responseInCaseOfUserNotFound(admin);
                if (response.getStatusCode() == HttpStatus.OK) {
                    User user = userRepository.findByUserName(usernameStr);
                    response = UserService.responseInCaseOfUserNotFound(user);
                    if (response.getStatusCode() == HttpStatus.OK) {
                        session.AdminAddParticipant(admin, user);
                        userRepository.save(user);
                        sessionRepository.save(session);
                        response = ResponseEntity.ok("user added");
                    }
                }
            }
        }
        catch (IllegalArgumentException e) {
            response= ResponseEntity.badRequest().body(e.getMessage());
        }
        return response;
    }
    public ResponseEntity<String> UserAddHimselfToSession(String id,String usernameStr)throws IllegalArgumentException {
        ResponseEntity<String> response;
        try {
            Session session = sessionRepository.findBySessionId(id);
            response= responseInCaseOfNotFoundOrDeletedOrExpired(session);
            if (response.getStatusCode()==HttpStatus.OK) {
                User user = userRepository.findByUserName(usernameStr);
                response = UserService.responseInCaseOfUserNotFound(user);
                if (response.getStatusCode() == HttpStatus.OK) {
                    session.addParticipant(user);
                    userRepository.save(user);
                    sessionRepository.save(session);
                    response = ResponseEntity.ok("user added");
                }
            }
        }
        catch (IllegalArgumentException e) {
            response= ResponseEntity.badRequest().body(e.getMessage());
        }
        return response;
    }
    public ResponseEntity<String> UserRemoveHimselfToSession(String id,String usernameStr)throws IllegalArgumentException {
        ResponseEntity<String> response;
        try {
            Session session = sessionRepository.findBySessionId(id);
            response = responseInCaseOfNotFoundOrDeletedOrExpired(session);
            if (response.getStatusCode()==HttpStatus.OK){
                User user=userRepository.findByUserName(usernameStr);
                response=UserService.responseInCaseOfUserNotFound(user);
                if (response.getStatusCode()==HttpStatus.OK){
                    Group group = session.getGroup();
                    session.removeParticipant(user);
                    userRepository.save(user);
                     if (session.isCanceled()) {
                            groupRepository.save(group);
                        }
                    sessionRepository.save(session);
                    response= ResponseEntity.ok("user removed");
                }
            }
        }
        catch (IllegalArgumentException e) {
            response= ResponseEntity.badRequest().body(e.getMessage());
        }
        return response;
    }
    public ResponseEntity<String> AdminRemoveUserFromSession(String id, String adminStr, String usernameStr)throws IllegalArgumentException {
        ResponseEntity<String> response;
        try {
            Session session = sessionRepository.findBySessionId(id);
            response = responseInCaseOfNotFoundOrDeletedOrExpired(session);
            if (response.getStatusCode() == HttpStatus.OK) {
                User admin = userRepository.findByUserName(adminStr);
                response = UserService.responseInCaseOfUserNotFound(admin);
                if (response.getStatusCode() == HttpStatus.OK) {
                    User user = userRepository.findByUserName(usernameStr);
                    response = UserService.responseInCaseOfUserNotFound(user);
                    if (response.getStatusCode() == HttpStatus.OK) {
                        Group group = session.getGroup();
                        session.removeParticipantByAdmin(admin, user);
                        userRepository.save(user);
                        if (session.isCanceled()) {
                            groupRepository.save(group);
                        }
                        sessionRepository.save(session);
                        return ResponseEntity.ok("user removed");
                    }
                }
            }
        }
        catch (IllegalArgumentException e) {
            response= ResponseEntity.badRequest().body(e.getMessage());
        }
        return response;
    }
    public ResponseEntity<String> setUserAsAdminOfSession(String id,String adminStr,String usernameStr)throws IllegalArgumentException {
        ResponseEntity<String> response;
        try {
            Session session = sessionRepository.findBySessionId(id);
            response = responseInCaseOfNotFoundOrDeletedOrExpired(session);
            if (response.getStatusCode()==HttpStatus.OK) {
                User admin = userRepository.findByUserName(adminStr);
                response = UserService.responseInCaseOfUserNotFound(admin);
                if (response.getStatusCode() == HttpStatus.OK) {
                    User user = userRepository.findByUserName(usernameStr);
                    response = UserService.responseInCaseOfUserNotFound(user);
                    if (response.getStatusCode() == HttpStatus.OK) {
                        session.setParticipantAsAdmin(admin, user);
                        sessionRepository.save(session);
                        response = ResponseEntity.ok("user is now admin");
                    }
                }
            }
        }
        catch (IllegalArgumentException e) {
            response= ResponseEntity.badRequest().body(e.getMessage());
        }
    return response;
    }
    public ResponseEntity<String> removeUserFromAdminOfSession(String id,String adminStr,String usernameStr)throws IllegalArgumentException {
        ResponseEntity<String> response;
        try {
            Session session = sessionRepository.findBySessionId(id);
            response = responseInCaseOfNotFoundOrDeletedOrExpired(session);
            if (response.getStatusCode() == HttpStatus.OK) {
                User admin = userRepository.findByUserName(adminStr);
                response = UserService.responseInCaseOfUserNotFound(admin);
                if (response.getStatusCode() == HttpStatus.OK) {
                    User user = userRepository.findByUserName(usernameStr);
                    response = UserService.responseInCaseOfUserNotFound(user);
                    if (response.getStatusCode() == HttpStatus.OK) {
                        session.adminRemoveParticipantFromBeingAdmin(admin, user);
                        sessionRepository.save(session);
                        response = ResponseEntity.ok("user is no longer admin admin");
                    }
                }
            }
        }
        catch (IllegalArgumentException e) {
            response= ResponseEntity.badRequest().body(e.getMessage());
        }
        return response;
    }
    public ResponseEntity<String> deleteSessionByAdmin(String id,String adminStr)throws IllegalArgumentException {
        ResponseEntity<String> response;
        try {
            Session session = sessionRepository.findBySessionId(id);
            response = responseInCaseOfNotFoundOrDeletedOrExpired(session);
            if (response.getStatusCode() == HttpStatus.OK) {
                User admin = userRepository.findByUserName(adminStr);
                response = UserService.responseInCaseOfUserNotFound(admin);
                if (response.getStatusCode() == HttpStatus.OK) {
                    List<User> sessionMembers = new ArrayList<>(session.getParticipants());
                    Group group = session.getGroup();
                    session.cancelMeetingByAdmin(admin);
                    for (User user : sessionMembers) {
                        userRepository.save(user);
                    }
                    groupRepository.save(group);
                    sessionRepository.save(session);
                    response = ResponseEntity.ok("session is deleted");
                }
            }
        }
        catch (IllegalArgumentException e) {
            response= ResponseEntity.badRequest().body(e.getMessage());
        }
        return response;
    }
    public ResponseEntity<String> setMaxParticipants(String id, String adminStr, String isLimited, String maxParticipantsStr)throws IllegalArgumentException {
        ResponseEntity<String> response;
        try {
            Session session = sessionRepository.findBySessionId( id);
            User admin=userRepository.findByUserName(adminStr);
            response= responseInCaseOfNotFoundOrDeletedOrExpired(session);
            if (response.getStatusCode()== HttpStatus.OK)
            {
                response= UserService.responseInCaseOfUserNotFound(admin);
                if (response.getStatusCode()== HttpStatus.OK)
                {
                    boolean limit = Boolean.parseBoolean(isLimited);
                    int maxParticipants = Integer.parseInt(maxParticipantsStr);
                    session.setLimitParticipants(admin,maxParticipants,limit);
                    sessionRepository.save(session);
                    response=ResponseEntity.ok("session set maxParticipants");
                }
            }
        }
        catch (IllegalArgumentException e) {
            response= ResponseEntity.badRequest().body(e.getMessage());
        }
        return response;
    }
    //this function return ResponseEntity.OK in case session is found and not deleted and not expired
    public static ResponseEntity<String> responseInCaseOfNotFoundOrDeletedOrExpired(Session s){
        ResponseEntity<String> response;
        if (s == null) {
            log.info("session not found");
            response = ResponseEntity.badRequest().body("session not found");
        } else if (s.isCanceled()) {
            log.info(String.format("session %s is already deleted",s.getSessionId()));
            response = ResponseEntity.badRequest().body("session not available");
        }
        else if (s.getSessionDate().before(Date.from(Instant.now()))){
            log.info(String.format("session %s is already expired",s.getSessionId()));
            response = ResponseEntity.badRequest().body(String.format("session %s is not available",s.getSessionId()));
        }
        else
        {
            response = ResponseEntity.ok(null);
        }
        return response;
    }
}
