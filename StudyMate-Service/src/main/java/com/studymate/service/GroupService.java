package com.studymate.service;


import com.studymate.dtos.GroupDto;
import com.studymate.dtos.SessionDto;
import com.studymate.model.Group;
import com.studymate.model.Session.Session;
import com.studymate.model.User;
import com.studymate.repositories.GroupRepository;
import com.studymate.repositories.SessionRepository;
import com.studymate.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GroupService {
    private static final Logger log = LogManager.getLogger(GroupService.class);
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessiosRepository;
    @Autowired
    public GroupService(GroupRepository groupRepository, UserRepository userRepository, SessionRepository sessioRepository ) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.sessiosRepository=sessioRepository;
    }
    public ResponseEntity<String> createGroup(String groupAdminStr ,String groupName, String institute, String curriculum,List<String> membersStr) {
        log.info("Creating group");
        ResponseEntity<String> response= ResponseEntity.ok().body(null);
        if (groupName ==null)
        {
              response= ResponseEntity.badRequest().body("groupName is null");
        }
        else if (institute ==null)
        {
              response= ResponseEntity.badRequest().body("institute is null");
        }
        else if (curriculum ==null)
        {
              response= ResponseEntity.badRequest().body("curriculum is null");
        }
        else if(groupRepository.findByGroupName(groupName)!= null) {
            String errorMsg = "Group name already exists";
            log.error(errorMsg);
            response= ResponseEntity.badRequest().body(errorMsg);
        }
        if (response.getStatusCode()==HttpStatus.OK) {
            try {
                User groupAdmin = userRepository.findByUserName(groupAdminStr);
                response = UserService.responseInCaseOfUserNotFound(groupAdmin);
                List<User> members = new ArrayList<>();
                for (String username : membersStr) {
                    User userToAdd = userRepository.findByUserName(username);
                    if (userToAdd != null) {
                        members.add(userToAdd);

                    } else
                    {
                        response = ResponseEntity.badRequest().body(String.format("could not find %s to add to group", username));
                    }
                }
                if (response.getStatusCode() == HttpStatus.OK) {
                    Date createdDate = new Date();
                    if (groupAdmin == null) {
                        response = ResponseEntity.badRequest().body("can not find group admin");
                    }
                    else {
                        Group group = new Group(groupName, institute, curriculum, createdDate, groupAdmin, members);
                        groupRepository.save(group);
                        for (User user : group.getMembers()) {
                            userRepository.save(user);
                        }
                        log.info("Group created successfully");
                        response = ResponseEntity.ok("Group created successfully");
                    }
                }
            } catch (Exception e) {
                log.error("Failed to create group: " + e.getMessage());
                response = ResponseEntity.badRequest().body("Failed to create group: " + e.getMessage());
            }
        }
        return response;
    }
    public ResponseEntity<List<String>> getAllGroupNames() {
        log.info("Getting all group names");
        try {
            List<String> groupNames = groupRepository.findAllGroupNames();
            log.info("All groups retrieved successfully");
            return ResponseEntity.ok(groupNames);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    public ResponseEntity<GroupDto> getGroup(String groupName) {
        log.info("Getting group");
         if (groupName ==null)
        {
            log.error(String.format("groupName can not be null"));
            return ResponseEntity.badRequest().build();
        }
        Group group = groupRepository.findByGroupName(groupName);
        if(group == null || group.isDeleted()) {
            log.error(String.format("Group %s not found", groupName));
            return ResponseEntity.badRequest().body(null);
        }
        else
        {
        GroupDto groupDto = new GroupDto(group.getGroupName(), group.getInstitute(),
                group.getCurriculum(), group.getAdminsNames(), group.getMembersNames());
        log.info("group retrieved successfully");
        return ResponseEntity.ok(groupDto);
        }
    }
    public ResponseEntity<String> addUserToGroup(String groupName,String adminCandidateStr ,String userToAddStr){
        ResponseEntity<String> response;
        try {
            Group group = groupRepository.findByGroupName(groupName);
            response = responseInCaseOfGroupNotFoundOrDeleted(group);
            if (response.getStatusCode() == HttpStatus.OK) {
                User adminCandidate = userRepository.findByUserName(adminCandidateStr);
                response = UserService.responseInCaseOfUserNotFound(adminCandidate);
                if (response.getStatusCode() == HttpStatus.OK) {
                    User userToAdd = userRepository.findByUserName(userToAddStr);
                    response = UserService.responseInCaseOfUserNotFound(userToAdd);
                    if (response.getStatusCode() == HttpStatus.OK) {
                        group.addMember(adminCandidate, userToAdd);
                        groupRepository.save(group);
                        userRepository.save(userToAdd);
                        return ResponseEntity.ok("User added");
                    }
                }
            }
        }
        catch (Exception e) {
            response= ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return response;
    }
    public ResponseEntity<String> addUserToBeGroupAdmin(String groupName,String adminCandidateStr ,String userToAddStr){
        ResponseEntity<String> response;
        try {
            Group group = groupRepository.findByGroupName(groupName);
            response = responseInCaseOfGroupNotFoundOrDeleted(group);
            if (response.getStatusCode() == HttpStatus.OK) {
                User adminCandidate = userRepository.findByUserName(adminCandidateStr);
                response = UserService.responseInCaseOfUserNotFound(adminCandidate);
                if (response.getStatusCode() == HttpStatus.OK) {
                    User userToAdd = userRepository.findByUserName(userToAddStr);
                    response = UserService.responseInCaseOfUserNotFound(userToAdd);
                    if (response.getStatusCode() == HttpStatus.OK) {
                        group.setMemberAsAdmin(adminCandidate, userToAdd);
                        groupRepository.save(group);
                        response = ResponseEntity.ok("User is admin now");
                    }
                }
            }
        }
        catch (Exception e) {
            log.error(e.getMessage());
            response= ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return response;
    }
    public ResponseEntity<String> removeUserFromGroup(String groupName,String adminCandidateStr ,String userToAddStr){
        ResponseEntity<String> response;
        try {
            Group group = groupRepository.findByGroupName(groupName);
            response = responseInCaseOfGroupNotFoundOrDeleted(group);
            if (response.getStatusCode() == HttpStatus.OK) {
                User adminCandidate = userRepository.findByUserName(adminCandidateStr);
                response = UserService.responseInCaseOfUserNotFound(adminCandidate);
                if (response.getStatusCode() == HttpStatus.OK) {
                    User userTorRemove = userRepository.findByUserName(userToAddStr);
                    response = UserService.responseInCaseOfUserNotFound(userTorRemove);
                    if (response.getStatusCode() == HttpStatus.OK) {
                        //TODO first need to delete user from all group sessions and see if they not should  be deleted
                        group.removeMemberByAdmin(adminCandidate, userTorRemove);
                        groupRepository.save(group);
                        userRepository.save(userTorRemove);
                        response = ResponseEntity.ok("User removed");
                    }
                }
            }
        }
        catch (Exception e) {
            log.error(e.getMessage());
            response= ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return response;
    }
    public ResponseEntity<String> removeUserFromGroupAdmin(String groupName,String adminCandidateStr ,String userToRemoveStr){
        ResponseEntity<String> response;
        try {
            Group group = groupRepository.findByGroupName(groupName);
            response=responseInCaseOfGroupNotFoundOrDeleted(group);
            if (response.getStatusCode()==HttpStatus.OK) {
                User adminCandidate = userRepository.findByUserName(adminCandidateStr);
                response = UserService.responseInCaseOfUserNotFound(adminCandidate);
                if (response.getStatusCode() == HttpStatus.OK) {
                    User userToRemoveFromBeingAdmin = userRepository.findByUserName(userToRemoveStr);
                    response = UserService.responseInCaseOfUserNotFound(userToRemoveFromBeingAdmin);
                    if (response.getStatusCode() == HttpStatus.OK) {
                        group.removeMemberFromBeingAdminByAdmin(adminCandidate,userToRemoveFromBeingAdmin);
                        groupRepository.save(group);
                        userRepository.save(userToRemoveFromBeingAdmin);
                        response= ResponseEntity.ok("User removed from being admin");
                    }
                }
            }
        }
        catch (Exception e) {
            log.error(e.getMessage());
            response= ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return response;
    }
    public ResponseEntity<String> deleteGroupByAdmin(String groupName,String adminCandidateStr ){
        ResponseEntity<String> response;
        try {
            Group group = groupRepository.findByGroupName(groupName);
            response = responseInCaseOfGroupNotFoundOrDeleted(group);
            if (response.getStatusCode() == HttpStatus.OK) {
                User adminCandidate = userRepository.findByUserName(adminCandidateStr);
                response = UserService.responseInCaseOfUserNotFound(adminCandidate);
                if (response.getStatusCode() == HttpStatus.OK) {
                    List<User> groupMembers = new ArrayList<>(group.getMembers());
                    group.removeAllMembersByAdmin(adminCandidate);
                    //TODO need to delete all group sessions
                    groupRepository.save(group);
                    for (User user : groupMembers) {
                        userRepository.save(user);
                    }
                    response = ResponseEntity.ok("delete group by admin");
                }
            }
        }
        catch (Exception e) {
            log.error(e.getMessage());
            response= ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return response;
    }
    public ResponseEntity<List<String>> getGroupsMember(String groupName){
        try {
            Group group = groupRepository.findByGroupName(groupName);
            if (group!=null && ! group.isDeleted())
            {
                List<String> membersNames= group.getMembersNames();
            return ResponseEntity.ok(membersNames);
            }
            else
            {
                return ResponseEntity.badRequest().body(null);
            }
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    public ResponseEntity<List<SessionDto>> getGroupSessions(String groupName){

        try {
            Group group = groupRepository.findByGroupName(groupName);
            if (group!= null && !group.isDeleted()) {
                List<Session> allSessions = group.getSessions();
                List<SessionDto> allSessionsDto = new ArrayList<>();
                for (Session session : allSessions) {
                    allSessionsDto.add(new SessionDto(session.getParticipantsName(), session.getAdminsName()
                            , session.getCreationDate(), session.getSessionDate(), session.getLocation(),
                            session.getDescription(), session.isLimitParticipants(), session.getGroup().getGroupName(), session.getMaxParticipants(), session.getSessionId()));
                }
                return ResponseEntity.ok(allSessionsDto);
            }
            else
                {
                    log.info("group not found, cant find group sessions");
                    return ResponseEntity.badRequest().body(null);
                }
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    //this function return ResponseEntity.OK in case group is found and not deleted
    public static ResponseEntity<String> responseInCaseOfGroupNotFoundOrDeleted(Group g)
    {
        ResponseEntity <String> response;
      if (g==null) {
          log.info("group not found");
          response= ResponseEntity.badRequest().body("group not found");
      }
      else  if (g.isDeleted()) {
          log.info("group already deleted");
          response= ResponseEntity.badRequest().body("group not available");
      }
      else
      {
          response=ResponseEntity.ok(null);
      }
      return response;
    }
}


