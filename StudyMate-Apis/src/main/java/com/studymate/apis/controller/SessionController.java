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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(URLMappingConstants.SESSION)
@RestController
//@CrossOrigins(origins = "http://localhost:3000", allowCredentials = "true")
public class SessionController
{
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

    @PostMapping(URLMappingConstants.ADD_USER_TO_SESSION)
    @ResponseBody
    public ResponseEntity<String> addUserToSession(@RequestHeader("Authorization") String token,@PathVariable String id , @PathVariable String username) {
        log.info("add user to session");
        return sessionService.AdminAddUserToSession(id,authenticationService.getUsernameFromToken(token),
                username);
    }

    @PostMapping(URLMappingConstants.USER_ADD_HIMSELF_TO_SESSION)
    @ResponseBody
    public ResponseEntity<String> userAddHimselfToSession(@RequestHeader("Authorization") String token,@PathVariable String id) {
        log.info("user added himself to session");
        return sessionService.UserAddHimselfToSession(id,authenticationService.getUsernameFromToken(token));
    }

    @PostMapping(URLMappingConstants.REMOVE_USER_FROM_SESSION)
    @ResponseBody
    public ResponseEntity<String> removeUSerFromSession(@RequestHeader("Authorization") String token,@PathVariable String id , @PathVariable String username) {
        log.info("remove user from session");
        return sessionService.AdminRemoveUserFromSession(id,authenticationService.getUsernameFromToken(token),
                username);
    }

    @PostMapping(URLMappingConstants.SET_USER_AS_ADMIN_OF_SESSION)
    @ResponseBody
    public ResponseEntity<String> AddUserToBeSessionAdmin(@RequestHeader("Authorization") String token,@PathVariable String id , @PathVariable String username) {
        log.info("Adding user to group");
         return sessionService.setUserAsAdminOfSession(id,authenticationService.getUsernameFromToken(token),
                username);
    }

    @PostMapping(URLMappingConstants.REMOVE_USER_FROM_ADMIN_OF_SESSION)
    @ResponseBody
    public ResponseEntity<String> removeUserFromBeingAdminOfGroup(@RequestHeader("Authorization") String token, @PathVariable String id , @PathVariable String username) {
        log.info("remove user from being admin of sessoin");
         return sessionService.removeUserFromAdminOfSession(id,authenticationService.getUsernameFromToken(token),
                username);    }

    @PostMapping(URLMappingConstants.DELETE_SESSION_BY_ADMIN)
    @ResponseBody
    public ResponseEntity<String> deleteSessionByAdmin(@RequestHeader("Authorization") String token,@PathVariable String id ) {
        log.info("deleting session");
         return sessionService.deleteSessionByAdmin(id,authenticationService.getUsernameFromToken(token));
    }

    @PostMapping(URLMappingConstants.SET_MAX_PARTICIPANTS_FOR_SESSION)
    @ResponseBody
    public ResponseEntity<String> setMAxParticipantsFroSession(@RequestHeader("Authorization") String token,@PathVariable String id ,@PathVariable String isLimited,@PathVariable String numberToLimit) {
        log.info("setting max participants for Session");
            return sessionService.setMaxParticipants(id,authenticationService.getUsernameFromToken(token),
                    isLimited,numberToLimit);

    }

}

