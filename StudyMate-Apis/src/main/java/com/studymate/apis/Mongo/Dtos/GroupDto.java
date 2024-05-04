package com.studymate.apis.Mongo.Dtos;
import Model.User;

import java.util.Date;
import java.util.List;

public class GroupDto {
    private String groupName;
    private String university;
    private String curriculum;
    private String groupAdmin;
    private Date createdDate;
    private List<String> members;

    public void setMembers(List<String> members) {
        this.members = members;
    }
    public List<String> getMembers() {
        return this.members ;
    }
    public void setGroupAdmin(String groupAdmin) {
        this.groupAdmin = groupAdmin;
    }
    public void setGroup(String groupName, String university, String curriculum, Date CreatedDate , List<String> allUsers){
    setGroupName(groupName);
    setUniversity(university);
    setCurriculum(curriculum);
    setCreatedDate(CreatedDate);
    setMembers(allUsers);
}
    public String getGroupAdmin() {
        return groupAdmin;
    }
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

    public void addUser(String userName) {
        members.add(userName);
    }
    public void removeUserFromGroup(User userName) {
        members.remove(userName);
    }

    public boolean containsUser(User userName) {
        return members.contains(userName);
    }
    public int getMembersCount() {
        return members.size();
    }

}

