package com.studymate.apis.Mongo.Dtos;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GroupDto {
    private String groupName;
    private String university;
    private String curriculum;
    private Date createdDate;
    private Map<String, UserDto> usersMap;

    // Constructor to initialize the group with properties and users
    public GroupDto(String groupName, String university, String curriculum, Date createdDate, Map<String, UserDto> usersMap) {
        this.groupName = groupName;
        this.university = university;
        this.curriculum = curriculum;
        this.createdDate = createdDate;
        this.usersMap = new HashMap<>(usersMap);
    }

    // Getters and setters for group properties

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(String curriculum) {
        this.curriculum = curriculum;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    // Method to add a user to the group
    public void addUser(String key, UserDto user) {
        usersMap.put(key, user);
    }

    // Method to get a user by username
    public UserDto getUser(String key) {
        return usersMap.get(key);
    }

    // Method to remove a user from the group
    public void removeUser(String key) {
        usersMap.remove(key);
    }

    // Method to check if a user exists in the group
    public boolean containsUser(String key) {
        return usersMap.containsKey(key);
    }

    // Method to get the number of users in the group
    public int getUserCount() {
        return usersMap.size();
    }

    // Method to get all users in the group
    public Map<String, UserDto> getAllUsers() {
        return new HashMap<>(usersMap);
    }
}
