package com.studymate.service;


import com.studymate.dtos.GroupDto;
import com.studymate.model.Group;
import com.studymate.model.User;
import com.studymate.repositories.GroupRepository;
import com.studymate.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class GroupService {
    private static final Logger log = LogManager.getLogger(GroupService.class);
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }


    public ResponseEntity<String> createGroup(String groupName, String institute, String curriculum, String groupAdminStr ,List<String> membersStr) {
        log.info("Creating group");

        if(groupRepository.findByGroupName(groupName)!= null) {
            String errorMsg = "Group name already exists";
            log.error(errorMsg);
            return ResponseEntity.badRequest().body(errorMsg);
        }

        try {
            // Additional checks can be added here if needed
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
    public ResponseEntity<List<String>> getAllGroupNames()
    {
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
                group.getCurriculum(), group.getGroupAdmin().getUserName(), group.getMembersNames());
        log.info("group retrieved successfully");
        return ResponseEntity.ok(groupDto);
    }


}


