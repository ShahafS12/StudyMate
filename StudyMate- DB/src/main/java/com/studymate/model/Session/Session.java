package com.studymate.model.Session;

import com.studymate.model.Group;
import com.studymate.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.time.Instant;

public class Session
{
    private static final Logger log = LogManager.getLogger(Session.class);

    private final UUID id;
    private Date sessionDate;
    private final boolean virtualSession=false;
    private String location;
    private List<User> participants;
    private List<User> sessionAdmins;
    private int maxParticipants;
    private String description;
    private boolean limitParticipants;
    private final User createdBy;
    private Group group;
    private final Date creationDate;
    private boolean isCanceled;
    public boolean isCanceled() {
        return isCanceled;
    }
    public Session(Date sessionDate,String location,List<User> participants,int maxParticipants,User createdBy,boolean limitParticipants,String description, Group group) {
        this.id = UUID.randomUUID();
        setSessionDate(sessionDate);
        this.creationDate = Date.from(Instant.now());
        checkCreatedBy(createdBy);
        this.createdBy = createdBy;
        this.sessionAdmins = new ArrayList<>();
        this.sessionAdmins.add(createdBy);
        this.participants = new ArrayList<>();
        this.participants.add(createdBy);
        createdBy.addSession(this);
        setMaxParticipants(maxParticipants);
        this.limitParticipants=limitParticipants;
        for(User participant : participants)
        {
            addParticipant(createdBy,participant);
        }
        setDescription(description);
        this.isCanceled = false;
        setGroup(createdBy,group);

        setDescription(description);
        this.location=location;

    }
    public void setGroup(User createdBy,Group group) throws IllegalArgumentException{
       if (!group.isMember(createdBy))
        {
            String message = String.format("Participant %s is not part of the group related to the session", createdBy.getUserName());
                log.error(message);
                throw new IllegalArgumentException(message);
        }
       this.group=group;
    }
    public void setSessionDate(Date sessionDate) throws IllegalArgumentException {
        if (sessionDate==null)
        {
            String errorMessage = String.format("can not set session %s date to NULL\n",id.toString());
             log.error(errorMessage);
             throw new IllegalArgumentException(errorMessage);
        }
        if (sessionDate.before(Date.from(Instant.now())))
         {
              String errorMessage = String.format("can not set session date to the past");
             log.error(errorMessage);
             throw new IllegalArgumentException(errorMessage);
         }
        this.sessionDate = sessionDate;
    }
    public void setMaxParticipants(int maxParticipants) throws IllegalArgumentException {
         if (maxParticipants<= 0)
        {
            String message = String.format("Can not set Max participants of session %s to zero or below",id.toString());
                log.error(message);
                throw new IllegalArgumentException(message);
        }

        if (maxParticipants<participants.size())
        {
            String message = String.format("Can not set Max participants number of session %s to %d. The number of participants in the session is already bigger",id.toString(),maxParticipants);
                log.error(message);
                throw new IllegalArgumentException(message);
        }
        this.maxParticipants = maxParticipants;
        log.info(String.format("max Participants in session %s set to %d ",id,maxParticipants));
    }
    public void checkCreatedBy(User createdBy) throws IllegalArgumentException {
        if (createdBy== null)
        {
            String message = String.format("Can not set creator of session %s NULL",id.toString());
                log.error(message);
                throw new IllegalArgumentException(message);
        }
    }
    public void setDescription(String description) throws IllegalArgumentException {
        if (description==null || description.trim().isEmpty())
        {
               String message = String.format("Description in session %s  can not be empty",id.toString());
                log.error(message);
                throw new IllegalArgumentException(message);
        }
        this.description = description;
            log.info(String.format("Description in session %s changed ",id.toString()));
    }
    public void addParticipant(User managerCandidate,User participant) throws IllegalArgumentException {
        checkIfUserIsAdmin(managerCandidate);
        if (participants.contains(participant)) {
            String message = String.format("Participant %s is already in session %s", participant.getUserName(), id.toString());
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        else if (limitParticipants == false || participants.size() < maxParticipants) {

            if (!participant.isInGroup(group)) {
                String message = String.format("can not add  %s to session %s.  %s is not in the group " +
                        "related to session ", participant.getUserName());
                log.error(message);
                throw new IllegalArgumentException(message);
            }
            participants.add(participant);
            participant.addSession(this);
            String message = String.format("Participant %s added to session: %s", participant.getUserName(), id.toString());
            log.info(message);
        } 
        else {
            String message = "Session is full";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    
    }
    public void removeParticipantByAdmin(User managerCandidate, User participant)throws IllegalArgumentException {
        checkIfUserIsAdmin(managerCandidate);
         log.info(String.format("Participant %s trying to remove %s from session %s "
                ,managerCandidate.getUserName(),participant.getUserName(),id.toString()));
        removeParticipant(participant);
    }
    public void setParticipantAsAdmin(User adminCandidate,User participant) throws IllegalArgumentException {
       log.info(String.format("Participant %s trying to set participant %s as admin of session %s"
               ,adminCandidate.getUserName() ,participant.getUserName(),id.toString()));
       checkIfUserIsParticipant(participant);
       checkIfUserIsAdmin(adminCandidate);
        sessionAdmins.add(participant);
        log.info(String.format("Participant %s is now admin of session %s"
                ,participant.getUserName(),id.toString()));
    }
    public void removeParticipant(User participant)throws IllegalArgumentException {
        checkIfUserIsParticipant(participant);
        boolean admin = sessionAdmins.contains(participant);
        if (admin)
        {
            removeParticipantFromBeingAdmin(participant);
        }
        participants.remove(participant);
        participant.deleteSession(this);
        log.info(String.format("Participant %s removed in session %s "
                ,participant.getUserName(),id.toString()));

        if (participants.size()==0)
        {
            log.info(String.format("Participants size is 0 deleting session %s", id.toString()));
            cancelMeeting();
        }
    }
    public void removeParticipantFromBeingAdmin(User admin)throws IllegalArgumentException {
        checkIfUserIsAdmin(admin);
         if (sessionAdmins.size()==1 ) { // the only admin want to not be admin
            User nextAdmin = null;
            int i=0;
            boolean found=false;
            while (i<participants.size() && !found) // look for the next participant to be admin
            {
                nextAdmin=participants.get(i);
                if (nextAdmin!=admin)
                {
                    found=true;
                }
                i++;
            }
            if (found)
            {
                setParticipantAsAdmin(admin, nextAdmin);
            }
         }
        sessionAdmins.remove(admin);
        log.info(String.format("Participant %s removed from being admin in session %s "
                ,admin.getUserName(),id.toString()));
    }
    public void cancelMeeting() {
        isCanceled = true;
         log.info(String.format("Canceling session %s",id.toString()));
    }
    public void checkIfUserIsAdmin(User managerCandidate) throws IllegalArgumentException{
        if (! sessionAdmins.contains(managerCandidate)){
              String message = String.format("Participant %s is not in an admin of the session", managerCandidate.getUserName());
                log.error(message);
                throw new IllegalArgumentException(message);
            }
    }
    public void checkIfUserIsParticipant(User participantCandidate) throws IllegalArgumentException{
        if (! participants.contains(participantCandidate)){
              String message = String.format("Participant %s is not in a participant of the session", participantCandidate.getUserName());
                log.error(message);
                throw new IllegalArgumentException(message);
            }
    }
    public void cancelMeetingByAdmin (User managerCandidate ){
        checkIfUserIsAdmin(managerCandidate);
        int size=participants.size();
        for (int i=0;i<size;i++)
        {
            User participant=participants.get(0);
            removeParticipant(participant);
        }
    }
    public String getDescription() {
        return description;
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
    public List<String> getParticipantsName() {
        List participantsName=new ArrayList<>();
        for (User participant:participants)
        {
            participantsName.add(participant.getUserName());
        }
        return participantsName;
    }
    public List<String> getAdminsName() {
        List adminsName=new ArrayList<>();
        for (User admin:sessionAdmins)
        {
            adminsName.add(admin.getUserName());
        }
        return adminsName;
    }
    public UUID getId(){
        return id;
    }
    public void setLocation(String location){
        this.location=location;
    }
    public String getLocation(){
        return location;
    }
}
