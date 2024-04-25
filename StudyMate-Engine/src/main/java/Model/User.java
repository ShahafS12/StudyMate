package Model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class User
{
    private static Logger log = LogManager.getLogger(Group.class);
    private String userName;
    private String password;
    private List<String> notifications; //todo check if we need to create notifications class
    private List<Session> sessions;
    private List<Group> groups;

    public User(String userName, String password) {
        //todo once we have a DB we need to check if the username is unique
        this.userName = userName;
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



}
