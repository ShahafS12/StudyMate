package com.studymate.apis.controller;
import com.studymate.dtos.GroupDto;
import com.studymate.service.GroupService;
import com.studymate.apis.constansts.URLMappingConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(URLMappingConstants.Group)
@RestController
//@CrossOrigins(origins = "http://localhost:3000", allowCredentials = "true")
public class GroupController
{
    private static final Logger log = LogManager.getLogger(GroupController.class);
    @Autowired
    private GroupService groupService;

    @PostMapping(URLMappingConstants.CREATE_GROUP)
    @ResponseBody
    public ResponseEntity<String> createGroup(@RequestBody GroupDto groupDto) {
        log.info("Creating Group");
        return groupService.createGroup(groupDto.getGroupName(), groupDto.getInstitute(),
                groupDto.getCurriculum(), groupDto.getGroupAdmin(),groupDto.getMembers()
                );
    }

    @GetMapping(URLMappingConstants.ALL_Group_NAMES)
    @ResponseBody
    public ResponseEntity<List<String>> GetAllGroupsNames() {
        log.info("Getting all groups names");
        return groupService.getAllGroupNames();
    }

    @GetMapping(URLMappingConstants.GET_Group)
    @ResponseBody
    public ResponseEntity<GroupDto> GetGroup(@PathVariable String groupName) {
        log.info("Getting group");
        return groupService.getGroup(groupName);
    }

}

