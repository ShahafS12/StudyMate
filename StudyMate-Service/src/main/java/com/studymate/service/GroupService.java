package com.studymate.service;

import Model.Group;
import Model.User;
import com.studymate.apis.Mongo.Dtos.UserDto;
import com.studymate.apis.Mongo.Repositories.GroupRepository;
import com.studymate.apis.Mongo.Dtos.GroupDto;
import com.studymate.apis.Mongo.Repositories.UserRepository;
import com.studymate.apis.controller.WebServicesController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GroupService {
    private static final Logger log = LogManager.getLogger(WebServicesController.class);
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository, UserService userService, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }


     public ResponseEntity<String> createGroup(String groupName, String university, String curriculum, String groupAdminStr ,List<String> membersStr) {
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
        Group group = new Group(groupName,university,curriculum,createdDate,groupAdmin, members);
        groupRepository.save(group);

        log.info("Group created successfully");
        return ResponseEntity.ok("Group created successfully");
    } catch (Exception e) {
        log.error("Failed to create group: " + e.getMessage());
        return ResponseEntity.badRequest().body("Failed to create group: " + e.getMessage());
    }
}

}


