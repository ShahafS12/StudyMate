package com.studymate.service;

import com.studymate.dtos.SearchResultDto;
import com.studymate.model.Group;
import com.studymate.model.User;
import com.studymate.repositories.GroupRepository;
import com.studymate.repositories.SessionRepository;
import com.studymate.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchBarService {
    private static final Logger log = LogManager.getLogger(GroupService.class);
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    @Autowired
    public SearchBarService(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }
    public List<SearchResultDto> search(String query) {
        log.info("Searching for {}", query);
        List<SearchResultDto> results = new ArrayList<>();
        List<Group> groupNames = groupRepository.findByGroupNameContainingIgnoreCase(query);
        for (Group group : groupNames) {
            results.add(new SearchResultDto(group.getGroupName(), "group"));
        }
        List<User> usernames = userRepository.findByUserNameContainingIgnoreCase(query);
        for (User user : usernames) {
            results.add(new SearchResultDto(user.getUserName(), "user"));
        }
        log.info("Found {} results", results.size());
        return results;
    }
}
