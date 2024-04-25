package Model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.time.Instant;

public class Session
{
    private static Logger log = LogManager.getLogger(Group.class);
    private UUID id;
    private Date sessionDate;
    private Date creationDate;
    private User createdBy;
    private List<User> participants;
    private int maxParticipants;
    private String description;
    private boolean isDeleted;
    private Group group;
    private final static int UNCAPPED_PARTICIPANTS = -1;
    //todo should we include location?
    public Session(Date sessionDate,User createdBy, int maxParticipants,String description, Group group) {
        this.id = UUID.randomUUID();
        this.sessionDate = sessionDate;
        this.creationDate = Date.from(Instant.now());
        this.createdBy = createdBy;
        this.participants = new ArrayList<>();
        this.maxParticipants = maxParticipants;
        this.description = description;
        this.isDeleted = false;
        this.group = group;
    }
    public Session(Date sessionDate,User createdBy, int maxParticipants,Group group) {
        this.id = UUID.randomUUID();
        this.sessionDate = sessionDate;
        this.creationDate = Date.from(Instant.now());
        this.createdBy = createdBy;
        this.participants = new ArrayList<>();
        this.maxParticipants = maxParticipants;
        this.description = "";
        this.isDeleted = false;
        this.group = group;
    }
    public Session(Date sessionDate,User createdBy, Group group) {
        this.id = UUID.randomUUID();
        this.sessionDate = sessionDate;
        this.creationDate = Date.from(Instant.now());
        this.createdBy = createdBy;
        this.participants = new ArrayList<>();
        this.maxParticipants = UNCAPPED_PARTICIPANTS;
        this.description = "";
        this.isDeleted = false;
        this.group = group;
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
                String message = String.format("Participant %s is already in the list", participant.getUserName());
                log.error(message);
                throw new IllegalArgumentException(message);
            }
            if(!participant.isInGroup(group)){
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
