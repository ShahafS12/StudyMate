package com.studymate.apis.controller;
import com.studymate.dtos.SessionDto;
import com.studymate.service.AuthenticationService;
import com.studymate.service.GroupService;
import com.studymate.apis.constansts.URLMappingConstants;
import com.studymate.service.SessionService;
import com.studymate.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.lang.model.element.UnknownAnnotationValueException;

import static org.ietf.jgss.GSSException.UNAUTHORIZED;

@RequestMapping(URLMappingConstants.SESSION)
@RestController
//@CrossOrigins(origins = "http://localhost:3000", allowCredentials = "true")
public class SessionController {
    private static final Logger log = LogManager.getLogger(GroupController.class);
    @Autowired
    private GroupService groupService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private UserService userService;

    @GetMapping(URLMappingConstants.GET_SESSION)
    @ResponseBody
    public ResponseEntity<SessionDto> getSession(@PathVariable String id) {
        log.info("getting session");
        return sessionService.getSession(id);
    }

    @PostMapping(URLMappingConstants.CREATE_SESSION)
    @ResponseBody
    public ResponseEntity<String> createSession(@RequestHeader("Authorization") String token, @RequestBody SessionDto sessionDto) {
        log.info("creating session");
        if (authenticationService.validateToken(token))
        {
        return sessionService.createSession(authenticationService.getUsernameFromToken(token), sessionDto);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }

    @PostMapping(URLMappingConstants.ADD_USER_TO_SESSION)
    @ResponseBody
    public ResponseEntity<String> addUserToSession(@RequestHeader("Authorization") String token, @PathVariable String sessionId, @PathVariable String username) {
        log.info("add user to session");
        if (authenticationService.validateToken(token)) {
            return sessionService.AdminAddUserToSession(sessionId, authenticationService.getUsernameFromToken(token),
                    username);
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }

    @PostMapping(URLMappingConstants.USER_ADD_HIMSELF_TO_SESSION)
    @ResponseBody
    public ResponseEntity<String> userAddHimselfToSession(@RequestHeader("Authorization") String token, @PathVariable("sessionId") String id) {
        if (authenticationService.validateToken(token)) {
            log.info("user added himself to session");
            return sessionService.UserAddHimselfToSession(id, authenticationService.getUsernameFromToken(token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }

    @PostMapping(URLMappingConstants.REMOVE_USER_FROM_SESSION)
    @ResponseBody
    public ResponseEntity<String> removeUSerFromSession(@RequestHeader("Authorization") String token, @PathVariable("sessionId") String id, @PathVariable("username") String username) {
        if (authenticationService.validateToken(token)) {
            log.info("remove user from session");
            return sessionService.AdminRemoveUserFromSession(id, authenticationService.getUsernameFromToken(token),
                    username);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }

    @PostMapping(URLMappingConstants.USER_REMOVE_HIMSELF_FROM_SESSION)
    @ResponseBody
    public ResponseEntity<String> userRemoveHimselfFromSession(@RequestHeader("Authorization") String token, @PathVariable("sessionId") String id) {
        log.info("user trying remove himself from session");
        ResponseEntity<String> response;
        if (authenticationService.validateToken(token)){

            response= sessionService.UserRemoveHimselfToSession(id, authenticationService.getUsernameFromToken(token));
        }
        else
        {
            response= ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
        return response;
    }

    @PostMapping(URLMappingConstants.SET_USER_AS_ADMIN_OF_SESSION)
    @ResponseBody
    public ResponseEntity<String> AddUserToBeSessionAdmin(@RequestHeader("Authorization") String token, @PathVariable("sessionId") String id, @PathVariable("username") String username) {
        log.info("Adding user to group");
      ResponseEntity<String> response;
        if (authenticationService.validateToken(token))
        {
            try
            {
                response= sessionService.setUserAsAdminOfSession(id, authenticationService.getUsernameFromToken(token),
                    username);
            }
            catch (Exception e)
            {
                response = ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        else
        {
            response = ResponseEntity.status(HttpStatusCode.valueOf(UNAUTHORIZED)).body("Authentication failed");
        }
        return response;
    }

    @PostMapping(URLMappingConstants.REMOVE_USER_FROM_ADMIN_OF_SESSION)
    @ResponseBody
    public ResponseEntity<String> removeUserFromBeingAdminOfGroup(@RequestHeader("Authorization") String token, @PathVariable("sessionId") String id, @PathVariable("username") String username) {
        log.info("remove user from being admin of sessoin");
        ResponseEntity<String> response;
        if (authenticationService.validateToken(token))
        {
            response= sessionService.removeUserFromAdminOfSession(id, authenticationService.getUsernameFromToken(token),
                    username);
        }
        else
        {
            response = ResponseEntity.status(HttpStatusCode.valueOf(UNAUTHORIZED)).body("Authentication failed");
        }
        return response;
    }


    @PostMapping(URLMappingConstants.DELETE_SESSION_BY_ADMIN)
    @ResponseBody
    public ResponseEntity<String> deleteSessionByAdmin(@RequestHeader("Authorization") String token, @PathVariable("sessionId") String id) {
        log.info("deleting session");
           ResponseEntity<String> response;
        if (authenticationService.validateToken(token)) {
            response = sessionService.deleteSessionByAdmin(id, authenticationService.getUsernameFromToken(token));
        }
        else
        {
            response = ResponseEntity.status(HttpStatusCode.valueOf(UNAUTHORIZED)).body("Authentication failed");
        }
        return response;
    }

    @PostMapping(URLMappingConstants.SET_MAX_PARTICIPANTS_FOR_SESSION)
    @ResponseBody
    public ResponseEntity<String> setMAxParticipantsFroSession(@RequestHeader("Authorization") String token, @PathVariable("sessionId") String id, @PathVariable("isLimited") String isLimited, @PathVariable("maxParticipants") String numberToLimit) {
        log.info("setting max participants for Session");
        ResponseEntity<String> response;
        if (authenticationService.validateToken(token)) {
            response = sessionService.setMaxParticipants(id, authenticationService.getUsernameFromToken(token),
                    isLimited, numberToLimit);
        }
        else
        {
            response = ResponseEntity.status(HttpStatusCode.valueOf(UNAUTHORIZED)).body("Authentication failed");
        }
        return response;
    }
}