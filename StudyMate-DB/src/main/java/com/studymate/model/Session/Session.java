package com.studymate.model.Session;

import com.studymate.model.Group;
import com.studymate.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.*;
import java.time.Instant;

public class Session
{
    private static final Logger log = LogManager.getLogger(Session.class);

    @Id
    private String sessionId;
    private Date sessionDate;
    private boolean virtualSession=false;
    private String location;
    private List<User> participants;
    private List<User> sessionAdmins;
    private int maxParticipants;
    private String description;
    private boolean limitParticipants;
    private User createdBy;
    private Group group;
    private Date creationDate;
    private boolean isCanceled;


    public boolean isCanceled() {
        return isCanceled;
    }
    public Session(Date sessionDate,String location,List<User> participants,int maxParticipants,User createdBy,boolean limitParticipants,String description, Group group) {
        this.sessionId = UUID.randomUUID().toString();
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
        setDescription(description);
        this.isCanceled = false;
        setGroup(createdBy,group);
        setDescription(description);
        this.location=location;
        if (participants!=null){
            for(User participant : participants)
            {
                AdminAddParticipant(createdBy,participant);
            }
        }
    }
    public Session(){}
    public void setGroup(User createdBy,Group group) throws IllegalArgumentException{
       if (!group.checkIfUserIsMember(createdBy))
        {
            String message = String.format("Participant %s is not part of the group related to the session", createdBy.getUserName());
                log.error(message);
                throw new IllegalArgumentException(message);
        }
       this.group=group;
       group.addSessionToGroup(this);
    }
    public void setSessionDate(Date sessionDate) throws IllegalArgumentException {
        if (sessionDate==null)
        {
            String errorMessage = String.format("can not set session %s date to NULL\n", sessionId.toString());
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
            String message = String.format("Can not set Max participants of session %s to zero or below", sessionId.toString());
                log.error(message);
                throw new IllegalArgumentException(message);
        }

        if (maxParticipants<participants.size())
        {
            String message = String.format("Can not set Max participants number of session %s to %d. The number of participants in the session is already bigger", sessionId.toString(),maxParticipants);
                log.error(message);
                throw new IllegalArgumentException(message);
        }
        this.maxParticipants = maxParticipants;
        log.info(String.format("max Participants in session %s set to %d ", sessionId,maxParticipants));
    }
    public void checkCreatedBy(User createdBy) throws IllegalArgumentException {
        if (createdBy== null)
        {
            String message = String.format("Can not set creator of session %s NULL", sessionId.toString());
                log.error(message);
                throw new IllegalArgumentException(message);
        }
    }
    public void setDescription(String description) throws IllegalArgumentException {
        if (description==null || description.trim().isEmpty())
        {
               String message = String.format("Description in session %s  can not be empty", sessionId.toString());
                log.error(message);
                throw new IllegalArgumentException(message);
        }
        this.description = description;
            log.info(String.format("Description in session %s changed ", sessionId.toString()));
    }
    public void AdminAddParticipant(User managerCandidate, User participant) throws IllegalArgumentException {
        if (checkIfUserIsAdmin(managerCandidate))
        {
            addParticipant(participant);
        }
        else
        {
            String message = String.format("Participant %s is not in an admin of the session, can not add participant", managerCandidate.getUserName());
                log.error(message);
                throw new IllegalArgumentException(message);

        }
    }
    public void addParticipant(User participant) throws IllegalArgumentException {
         if (!participant.isInGroup(group)) {
                String message = String.format("can not add  %s to session %s.  %s is not in the group %s"
                         , participant.getUserName(),sessionId,participant.getUserName(),group.getGroupName());
                log.error(message);
                throw new IllegalArgumentException(message);
         }
         else if (!isLimitParticipants() || participants.size() < maxParticipants) {
             if (participants.contains(participant)){
                 String message = String.format("Participant %s is already in session %s",
                         participant.getUserName(), sessionId.toString());
                log.error(message);
                throw new IllegalArgumentException(message);
             }
             else {
                 participants.add(participant);
                 participant.addSession(this);
                 String message = String.format("Participant %s added to session: %s", participant.getUserName(), sessionId.toString());
                 log.info(message);

             }
        }
        else {
            String message = "Session is full";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }
    public void removeParticipantByAdmin(User managerCandidate, User participant)throws IllegalArgumentException {
        if (checkIfUserIsAdmin(managerCandidate)) {
            log.info(String.format("Participant %s trying to remove %s from session %s "
                    , managerCandidate.getUserName(), participant.getUserName(), sessionId.toString()));
            removeParticipant(participant);
        }
        else
        {
            String message = String.format("Participant %s is not in an admin of the session, can not remove someone from being admin", managerCandidate.getUserName());
                log.error(message);
                throw new IllegalArgumentException(message);

        }
    }
    public void setParticipantAsAdmin(User adminCandidate,User participant) throws IllegalArgumentException {
       log.info(String.format("Participant %s trying to set participant %s as admin of session %s"
               ,adminCandidate.getUserName() ,participant.getUserName(), sessionId.toString()));
       if (checkIfUserIsParticipant(participant)) {
           if (checkIfUserIsAdmin(adminCandidate)) {
               if (checkIfUserIsAdmin(participant))
               {
                    String message = String.format("Participant %s is already  an admin of the session can not set participant as  admin", participant.getUserName());
                   log.error(message);
                   throw new IllegalArgumentException(message);
               }
               else
               {
                    sessionAdmins.add(participant);
                   log.info(String.format("Participant %s is now admin of session %s"
                           , participant.getUserName(), sessionId.toString()));
               }
           }
           else {
               String message = String.format("Participant %s is not in an admin of the session can not set participant as  admin", adminCandidate.getUserName());
               log.error(message);
               throw new IllegalArgumentException(message);
           }
       }
       else
        {
            String message = String.format("user %s is not in an participant of the session, can not set him as admin of meeting", participant.getUserName());
                log.error(message);
                throw new IllegalArgumentException(message);
        }

    }
    public void removeParticipant(User participant)throws IllegalArgumentException {
        if (checkIfUserIsParticipant(participant)) {
            boolean admin = sessionAdmins.contains(participant);
            if (admin) {
                removeParticipantFromBeingAdmin(participant);
            }
            participants.remove(participant);
            participant.deleteSession(this);
            log.info(String.format("Participant %s removed in session %s "
                    , participant.getUserName(), sessionId.toString()));

            if (participants.size() == 0) {
                log.info(String.format("Participants size is 0 deleting session %s", sessionId.toString()));
                cancelMeeting();
            }
        }
            else
        {
            String message = String.format("user %s is not in an participant of the session, can not remove him", participant.getUserName());
                log.error(message);
                throw new IllegalArgumentException(message);
        }
    }
    public void removeParticipantFromBeingAdmin(User admin)throws IllegalArgumentException {
        if (checkIfUserIsAdmin(admin)) {
            if (sessionAdmins.size() == 1) { // the only admin want to not be admin
                User nextAdmin = null;
                int i = 0;
                boolean found = false;
                while (i < participants.size() && !found) // look for the next participant to be admin
                {
                    nextAdmin = participants.get(i);
                    if (nextAdmin != admin) {
                        found = true;
                    }
                    i++;
                }
                if (found) {
                    setParticipantAsAdmin(admin, nextAdmin);
                }
            }
            sessionAdmins.remove(admin);
            log.info(String.format("Participant %s removed from being admin in session %s "
                    , admin.getUserName(), sessionId.toString()));
        }
        else
        {
            String message = String.format("Participant %s is not in an admin of the session can not remove himself from being admin", admin.getUserName());
                log.error(message);
                throw new IllegalArgumentException(message);
        }
    }
    public void adminRemoveParticipantFromBeingAdmin(User admin,User anotherAdmin)throws IllegalArgumentException {
        if (checkIfUserIsAdmin(admin))
        {
            removeParticipantFromBeingAdmin(anotherAdmin);
            log.info(String.format("Participant %s removed user %s from being admin in session %s "
                    , admin.getUserName(), anotherAdmin.getUserName(), sessionId.toString()));
        }
        else
        {
            String message = String.format("Participant %s is not in an admin of the session can not remove participant from being admin", admin.getUserName());
                log.error(message);
                throw new IllegalArgumentException(message);

        }
    }
    public void cancelMeeting() {
        if (isCanceled==false){
            isCanceled = true;
            group.removeSession(this);
            log.info(String.format("Canceling session %s", sessionId.toString()));
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session otherSession = (Session) o;
        return Objects.equals(this.sessionId, ((Session) o).sessionId);
    }
    public boolean checkIfUserIsAdmin(User managerCandidate) {
        if (managerCandidate==null)
        {
            return false;
        }
        return sessionAdmins.contains(managerCandidate);
    }
    public boolean checkIfUserIsParticipant(User participantCandidate) {
        if (participantCandidate==null)
        {
            return false;
        }
        return participants.contains(participantCandidate);
    }
    public void cancelMeetingByAdmin (User managerCandidate ){
        if (checkIfUserIsAdmin(managerCandidate)) {
            int size = participants.size();
            for (int i = 0; i < size; i++) {
                User participant = participants.get(0);
                removeParticipant(participant);
            }
            //cancelMeeting(); no need cause remove participants lead to cancel meeting
        }
        else
        {
            String message = String.format("Participant %s is not in an admin of the session, can not cancel meeting", managerCandidate.getUserName());
                log.error(message);
                throw new IllegalArgumentException(message);
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
    public String getSessionId(){
        return sessionId;
    }
    public void setLocation(String location){
        this.location=location;
    }
    public String getLocation(){
        return location;
    }
    public void setLimitParticipants(User admin ,int maxParticipants,boolean limitParticipants) {
        if (checkIfUserIsAdmin(admin)) {
            this.limitParticipants = limitParticipants;
            if (limitParticipants) {
                setMaxParticipants(maxParticipants);
            }
        }
    else
        {
            String message = String.format("Participant %s is not in an admin of the session, can not set max participant ", admin.getUserName());
                log.error(message);
                throw new IllegalArgumentException(message);

        }
    }
    public boolean isLimitParticipants() {
        return limitParticipants;
    }
    public Group getGroup() {
        return group;
    }
    public int getMaxParticipants() {
        return maxParticipants;
    }
    public List<User> getSessionAdmins() {
        return sessionAdmins;
    }
    public boolean getIsVirtualSession() {
        return virtualSession;
    }
    public boolean isVirtualSession() {
        return virtualSession;
    }
    public void setVirtualSession(boolean virtualSession) {
        this.virtualSession = virtualSession;
    }


}
