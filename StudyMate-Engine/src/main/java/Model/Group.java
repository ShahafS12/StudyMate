package Model;

import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Group
{
    private static final Logger log = LogManager.getLogger(Group.class);
    private String name;
    private String institute;
    private String curriculum;
    private List<User> members;
    private List<Session> sessions;
    public Group(String name, String institute, String curriculum) {
        this.name = name;
        this.institute = institute;
        this.curriculum = curriculum;
        this.members = new ArrayList<>();
        this.sessions = new ArrayList<>();
    }
    public void addMember(User user) throws IllegalArgumentException{
        if (!members.contains(user)) {
            members.add(user);
            if(user.isInGroup(this))
                user.addGroup(this);
            log.info(String.format("User %s added to group %s", user.getUserName(), name));
        }
        else {
            String message = String.format("User %s already in group %s", user.getUserName(), name);
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }
    public void removeMember(User user) throws IllegalArgumentException{
        if (members.contains(user)) {
            members.remove(user);
            log.info(String.format("User %s removed from group %s", user.getUserName(), name));
        }
        else {
            String message = String.format("User %s not in group %s", user.getUserName(), name);
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    public void setName(String name) {
        //todo once we have a DB we need to validate name+institute+curriculum is unique
        this.name = name;
    }
    public void addSession(Session session) {
        sessions.add(session);
    }

    public String getName() {
        return name;
    }
    public boolean isMember(User user) {
        return members.contains(user);
    }

}
