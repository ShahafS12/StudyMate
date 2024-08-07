package com.studymate.dtos;

import java.util.Date;
import java.util.List;

public class GroupDto {
    private String groupName;
    private String institute;
    private String curriculum;
    private List<String> groupAdmins;
    private Date createdDate;
    private List<String> members;

    public GroupDto(String groupName, String institute, String curriculum, List<String> groupAdmin, List<String> members) {
        this.groupName = groupName;
        this.institute = institute;
        this.curriculum = curriculum;
        this.groupAdmins = groupAdmin;
        this.members = members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
    public List<String> getMembers() {
        return this.members ;
    }
    public void setGroupAdmins(List<String> groupAdmins) {
        this.groupAdmins = groupAdmins;
    }
    public void setGroup(String groupName, String institute, String curriculum, Date CreatedDate , List<String> allUsers){
    setGroupName(groupName);
    setInstitute(institute);
    setCurriculum(curriculum);
    setCreatedDate(CreatedDate);
    setMembers(allUsers);
}
    public List<String> getGroupAdmins() {
        return groupAdmins;
    }
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
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
    public void removeUserFromGroup(String userName) {
        members.remove(userName);
    }

    public boolean containsUser(String userName) {
        return members.contains(userName);
    }
    public int getMembersCount() {
        return members.size();
    }

}

