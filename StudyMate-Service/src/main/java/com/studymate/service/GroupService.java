package com.studymate.service;


import com.studymate.dtos.GroupDto;
import com.studymate.dtos.SessionDto;
import com.studymate.model.Group;
import com.studymate.model.Session.Session;
import com.studymate.model.User;
import com.studymate.repositories.GroupRepository;
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


    @Autowired
    public GroupService(GroupRepository groupRepository, UserRepository userRepository ) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }
    public ResponseEntity<String> createGroup(String groupAdminStr ,String groupName, String institute, String curriculum,List<String> membersStr) {
        log.info("Creating group");
        if(groupRepository.findByGroupName(groupName)!= null) {
            String errorMsg = "Group name already exists";
            log.error(errorMsg);
            return ResponseEntity.badRequest().body(errorMsg);
        }

        try {
            User groupAdmin=userRepository.findByUserName(groupAdminStr);
            List<User> members= userRepository.findMembersByUserName(membersStr);
            Date createdDate=new Date();
            Group group = new Group(groupName,institute,curriculum,createdDate,groupAdmin, members);
            groupRepository.save(group);
            log.info("Group created successfully");
            return ResponseEntity.ok("Group created successfully");
        }
        catch (Exception e) {
            log.error("Failed to create group: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to create group: " + e.getMessage());
        }
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
        Group group = groupRepository.findByGroupName(groupName);
        if(group == null) {
            log.error(String.format("Group %s not found", groupName));
            return ResponseEntity.badRequest().body(null);
        }
        GroupDto groupDto = new GroupDto(group.getGroupName(), group.getInstitute(),
                group.getCurriculum(), group.getAdminsNames(), group.getMembersNames());
        log.info("group retrieved successfully");
        return ResponseEntity.ok(groupDto);
    }
    public ResponseEntity<String> addUserToGroup(String groupName,String adminCandidateStr ,String userToAddStr){
        try {
            Group group = groupRepository.findByGroupName(groupName);
            User adminCandidate=userRepository.findByUserName(adminCandidateStr);
            User userToAdd=userRepository.findByUserName(userToAddStr);
            group.addMember(adminCandidate,userToAdd);
            groupRepository.save(group);
            userRepository.save(userToAdd);
            return ResponseEntity.ok("User added");
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    public ResponseEntity<String> addUserToBeGroupAdmin(String groupName,String adminCandidateStr ,String userToAddStr){
        try {
            Group group = groupRepository.findByGroupName(groupName);
            User adminCandidate=userRepository.findByUserName(adminCandidateStr);
            User userToAdd=userRepository.findByUserName(userToAddStr);
            group.setMemberAsAdmin(adminCandidate,userToAdd);
            groupRepository.save(group);
            userRepository.save(userToAdd);
            return ResponseEntity.ok("User is admin now");
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    public ResponseEntity<String> removeUserFromGroup(String groupName,String adminCandidateStr ,String userToAddStr){
        try {
            Group group = groupRepository.findByGroupName(groupName);
            User adminCandidate=userRepository.findByUserName(adminCandidateStr);
            User userTorRemove=userRepository.findByUserName(userToAddStr);
            group.removeMemberByAdmin(adminCandidate,userTorRemove);
            groupRepository.save(group);
            userRepository.save(userTorRemove);
            return ResponseEntity.ok("User removed");
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    public ResponseEntity<String> removeUserFromGroupAdmin(String groupName,String adminCandidateStr ,String userToAddStr){
        try {
            Group group = groupRepository.findByGroupName(groupName);
            User adminCandidate=userRepository.findByUserName(adminCandidateStr);
            User userTorRemove=userRepository.findByUserName(userToAddStr);
            group.removeMemberFromBeingAdminByAdmin(adminCandidate,userTorRemove);
            groupRepository.save(group);
            userRepository.save(userTorRemove);
            return ResponseEntity.ok("User removed");
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    public ResponseEntity<String> deleteGroupByAdmin(String groupName,String adminCandidateStr ){
        try {
            Group group = groupRepository.findByGroupName(groupName);
            User adminCandidate=userRepository.findByUserName(adminCandidateStr);
            group.deleteGroupByAdmin(adminCandidate);
            groupRepository.save(group);
            return ResponseEntity.ok("delete group");
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    public ResponseEntity<List<String>> getGroupsMember(String groupName){
        try {
            Group group = groupRepository.findByGroupName(groupName);
            List<String> membersNames= group.getMembersNames();
            return ResponseEntity.ok(membersNames);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    public ResponseEntity<List<SessionDto>> getGroupSessions(String groupName){
        try {
            Group group = groupRepository.findByGroupName(groupName);
            List<Session> allSessions= group.getSessions();
            List<SessionDto>allSessionsDto=new ArrayList<>();
            for(Session session:allSessions)
            {
                allSessionsDto.add(new SessionDto(session.getParticipantsName(),session.getAdminsName()
                ,session.getCreationDate(),session.getSessionDate(),session.getLocation(),
                 session.getSessionId(),session.getDescription()));
            }

            return ResponseEntity.ok(allSessionsDto);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }





}


