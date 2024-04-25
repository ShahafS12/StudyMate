package Model;

import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Group
{
    private static Logger log = LogManager.getLogger(Group.class);
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
    public void addMember(User user) {
        if (!members.contains(user)) {
            members.add(user);
            log.info(String.format("User %s added to group %s", user.getUserName(), name));
        }
        else {
            log.error(String.format("User %s already in group %s", user.getUserName(), name));
        }
    }
    public void addSession(Session session) {
        sessions.add(session);
    }

}
