package com.studymate.dtos;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SessionDto {
    private List<String> membersName;
    private List<String> adminsName;
    private Date createdDate;
    private Date sessionDate;
    private String location;
    private String groupName;
    private String id;
    private String description;
    private int maxParticipants;
    private boolean isLimited;

    public SessionDto(List<String> membersName,List<String> adminsName, Date createdDate, Date sessionDate, String location
     ,String description, boolean isLimited, String groupName, int maxParticipants,String id) {
        this.membersName = membersName;
        this.groupName=groupName;
        this.maxParticipants=maxParticipants;
        this.adminsName = adminsName;
        this.createdDate = createdDate;
        this.sessionDate = sessionDate;
        this.location = location;
        this.id = id;
        this.description = description;
        this.isLimited=isLimited;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }
        public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public boolean isLimited() {
        return isLimited;
    }

    public void setLimited(boolean limited) {
        isLimited = limited;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }


    public Date getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(Date sessionDate) {
        this.sessionDate = sessionDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }
     public List<String> getMembersName() {
        return membersName;
    }

    public void setMembersName(List<String> membersName) {
        this.membersName = membersName;
    }
    public List<String> getAdminsName() {
        return adminsName;
    }

    public void setAdminsName(List<String> adminsName) {
        this.adminsName = adminsName;
    }






}
