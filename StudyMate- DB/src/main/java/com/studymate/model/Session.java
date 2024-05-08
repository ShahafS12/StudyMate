package com.studymate.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.time.Instant;

public class Session
{
    private static final Logger log = LogManager.getLogger(Group.class);
    private final UUID id;
    private Date sessionDate;
    private final Date creationDate;
    private final User createdBy;
    private List<User> participants;//todo maybe this should be a map with user and role
    private int maxParticipants;
    private String description;
    private boolean isDeleted;
    private final Group group;
    private final static int UNCAPPED_PARTICIPANTS = -1;
    //todo should we include location? last modified? permissions?
    public Session(Date sessionDate,User createdBy, int maxParticipants,String description, Group group) {
        validateParameters(sessionDate, createdBy, group);
        this.id = UUID.randomUUID();
        this.sessionDate = sessionDate;
        this.creationDate = Date.from(Instant.now());
        this.createdBy = createdBy;
        this.participants = new ArrayList<>();
        participants.add(createdBy);
        this.maxParticipants = maxParticipants;
        this.description = description;
        this.isDeleted = false;
        this.group = group;
    }
    public Session(Date sessionDate,User createdBy,Group group, int maxParticipants) {
        validateParameters(sessionDate, createdBy, group);
        this.id = UUID.randomUUID();
        this.sessionDate = sessionDate;
        this.creationDate = Date.from(Instant.now());
        this.createdBy = createdBy;
        this.participants = new ArrayList<>();
        participants.add(createdBy);
        this.maxParticipants = maxParticipants;
        this.description = "";
        this.isDeleted = false;
        this.group = group;
    }
    public Session(Date sessionDate,User createdBy, Group group) {
        validateParameters(sessionDate, createdBy, group);
        this.id = UUID.randomUUID();
        this.sessionDate = sessionDate;
        this.creationDate = Date.from(Instant.now());
        this.createdBy = createdBy;
        this.participants = new ArrayList<>();
        participants.add(createdBy);
        this.maxParticipants = UNCAPPED_PARTICIPANTS;
        this.description = "";
        this.isDeleted = false;
        this.group = group;
    }
    private void validateParameters(Date sessionDate,User createdBy, Group group) throws IllegalArgumentException{
        StringBuilder errorMessage = new StringBuilder();
        if(sessionDate == null || createdBy == null || group == null) {
            errorMessage.append("The following parameters are required: ");
            if(sessionDate == null) {
                errorMessage.append("sessionDate ");
            }
            if(createdBy == null) {
                errorMessage.append("createdBy ");
            }
            if(group == null) {
                errorMessage.append("group ");
            }
        }
        else {
            if (sessionDate.before(Date.from(Instant.now()))) {
                errorMessage.append("Session date is in the past\n");
            }
            if (!createdBy.isInGroup(group)) {
                errorMessage.append(String.format("User %s is not in group %s\n", createdBy.getUserName(), group.getGroupName()));
            }
        }
        if(errorMessage.length() > 0) {
            log.error(errorMessage.toString());
            throw new IllegalArgumentException(errorMessage.toString());
        }
    }
    public void setSessionDate(Date sessionDate) {
        this.sessionDate = sessionDate;
    }
    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void addParticipant(User participant) throws IllegalArgumentException {
        if (maxParticipants == UNCAPPED_PARTICIPANTS || participants.size() < maxParticipants) {
            if(participants.contains(participant)){
                String message = String.format("Participant %s is already in session", participant.getUserName());
                log.error(message);
                throw new IllegalArgumentException(message);
            }
            if(participant.isInGroup(group)){
                String message = String.format("Participant %s is not in the group", participant.getUserName());
                log.error(message);
                throw new IllegalArgumentException(message);
            }
            participants.add(participant);
            String message = String.format("Participant %s added to session: %s", participant.getUserName(), id.toString());
            log.info(message);
        }
        else {
            String message = "Session is full";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }
    public void removeParticipant(User participant) {
        participants.remove(participant);
    }
    public void deleteSession() {
        isDeleted = true;
    }
    public String getDescription() {
        return description;
    }
    public UUID getId() {
        return id;
    }
    public Date getSessionDate() {
        return sessionDate;
    }
    public Date getCreationDate() {
        return creationDate;
    }
    public User getCreatedBy() {
        return createdBy;
    }
    public List<User> getParticipants() {
        return participants;
    }

}
