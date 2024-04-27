package Model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User
{
    private static final Logger log = LogManager.getLogger(Group.class);
    private String userName;
    private String password;
    private final Date memberSince;
    private List<String> notifications; //todo check if we need to create notifications class to include link to relevant object,date,viewed etc
    private List<Session> sessions;
    private List<Group> groups;
    //todo add profile picture, session history

    public User(String userName, String password) {
        //todo once we have a DB we need to check if the username is unique
        this.userName = userName;
        this.memberSince = Date.from(Instant.now());
        this.password = password;
        this.notifications = new ArrayList<>();
        this.sessions = new ArrayList<>();
        this.groups = new ArrayList<>();
    }
    public String getUserName() {
        return userName;
    }
    public boolean isInGroup(Group group) {
        return groups.contains(group);
    }
    public void addGroup(Group group) throws IllegalArgumentException{
        if (!groups.contains(group)) {
            groups.add(group);
            if(!group.isMember(this))
                group.addMember(this);
            log.info(String.format("User %s added to group %s", userName, group.getName()));
        }
        else {
            String message = String.format("User %s already in group %s", userName, group.getName());
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }
    public void addSession(Session session) {
        sessions.add(session);
    }
    public void deleteSession(Session session) throws IllegalArgumentException {
        if (sessions.contains(session)) {
            sessions.remove(session);
        }
        else {
            String message = String.format("User %s tried to delete session %s that they are not in", userName, session.getId());
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }



}
