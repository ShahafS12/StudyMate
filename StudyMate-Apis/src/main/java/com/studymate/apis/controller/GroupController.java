package com.studymate.apis.controller;
import com.studymate.dtos.GroupDto;
import com.studymate.dtos.SessionDto;
import com.studymate.dtos.UserDto;
import com.studymate.service.AuthenticationService;
import com.studymate.service.GroupService;
import com.studymate.apis.constansts.URLMappingConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(URLMappingConstants.GROUP)
@RestController
//@CrossOrigins(origins = "http://localhost:3000", allowCredentials = "true")
public class GroupController
{
    private static final Logger log = LogManager.getLogger(GroupController.class);
    @Autowired
    private GroupService groupService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(URLMappingConstants.CREATE_GROUP)
    @ResponseBody
    public ResponseEntity<String> createGroup(@RequestHeader("Authorization") String token,@RequestBody GroupDto groupDto) {
        log.info("Creating Group");
        List<String> names=groupDto.getMembers();
        authenticationService.validateToken(token);
        String name=authenticationService.getUsernameFromToken(token);
        return groupService.createGroup(authenticationService.getUsernameFromToken(token),groupDto.getGroupName(), groupDto.getInstitute(),
                groupDto.getCurriculum(),groupDto.getMembers()
                );
    }

    @GetMapping(URLMappingConstants.ALL_GROUP_NAMES)
    @ResponseBody
    public ResponseEntity<List<String>> GetAllGroupsNames() {
        log.info("Getting all groups names");
        return groupService.getAllGroupNames();
    }

    @GetMapping(URLMappingConstants.GET_GROUP)
    @ResponseBody
    public ResponseEntity<GroupDto> GetGroup(@PathVariable String groupName) {
        log.info("Getting group");
        return groupService.getGroup(groupName);
    }

    @PostMapping(URLMappingConstants.ADD_USER_TO_GROUP)
    @ResponseBody
    public ResponseEntity<String> AddUserToGroup(@RequestHeader("Authorization") String token,@PathVariable String groupName , @PathVariable String username) {
        log.info("Adding user to group");
         return groupService.addUserToGroup(groupName,
                authenticationService.getUsernameFromToken(token),username);
    }

    @PostMapping(URLMappingConstants.REMOVE_USER_FROM_GROUP)
    @ResponseBody
    public ResponseEntity<String> removeUserToGroup(@RequestHeader("Authorization") String token,@PathVariable String groupName , @PathVariable String username) {
        log.info("remove user from group");
         return groupService.removeUserFromGroup(groupName,
                authenticationService.getUsernameFromToken(token),username);
    }

    @PostMapping(URLMappingConstants.SET_USER_AS_ADMIN_OF_GROUP)
    @ResponseBody
    public ResponseEntity<String> AddUserToBeAdminOfGroup(@RequestHeader("Authorization") String token,@PathVariable String groupName , @PathVariable String username) {
        log.info("Adding user to be a group's admin");
         return groupService.addUserToBeGroupAdmin(groupName,
                authenticationService.getUsernameFromToken(token),username);
    }

    @PostMapping(URLMappingConstants.REMOVE_USER_AS_ADMIN_OF_GROUP)
    @ResponseBody
    public ResponseEntity<String> removeUserFromBeingAdminOfGroup(@RequestHeader("Authorization") String token,@PathVariable String groupName , @PathVariable String username) {
        log.info("remove user from being admin of the group");
         return groupService.removeUserFromGroupAdmin(groupName,
                authenticationService.getUsernameFromToken(token),username);
    }

    @PostMapping(URLMappingConstants.DELETE_GROUP_BY_ADMIN)
    @ResponseBody
    public ResponseEntity<String> deleteGroupByAdmin(@RequestHeader("Authorization") String token,@PathVariable String groupName) {
        log.info("deleting Group");
         return groupService.deleteGroupByAdmin(groupName,authenticationService.getUsernameFromToken(token));
    }

     @GetMapping(URLMappingConstants.GET_GROUP_SESSIONS)
    @ResponseBody
    public ResponseEntity<List<SessionDto>> getGroupSessions(@PathVariable String groupName) {
        log.info("getting all sessions");
         return groupService.getGroupSessions(groupName);
    }

    @GetMapping(URLMappingConstants.GET_GROUP_MEMBERS)
    @ResponseBody
    public ResponseEntity<List<String>> getGroupMembers(@PathVariable String groupName) {
        log.info("getting all group members");
         return groupService.getGroupsMember(groupName);
    }

}

