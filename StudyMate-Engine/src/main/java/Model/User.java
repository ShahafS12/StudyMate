package Model;

import java.util.ArrayList;
import java.util.List;

public class User
{
    private String userName;
    private String password;
    private List<String> notifications; //todo check if we need to create notifications class
    private List<Session> sessions;
    private List<Group> groups;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.notifications = new ArrayList<>();
        this.sessions = new ArrayList<>();
        this.groups = new ArrayList<>();
    }


}
