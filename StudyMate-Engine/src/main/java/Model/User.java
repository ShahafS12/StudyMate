package Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
public class User
{
    private static final Logger log = LogManager.getLogger(Group.class);
    @Id
    private String id;
    @Indexed(unique = true)
    private String userName;
    private String password;
    private String email;
    private String university;
    private String degree;
    private String curriculum;
    private String memberSince;
    private List<String> notifications; //todo check if we need to create notifications class to include link to relevant object,date,viewed etc
    private List<Session> sessions;
    private List<Group> groups;
    private String gender;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    //todo add profile picture, session history

    public User(String userName, String password, String email, String university, String degree, String curriculum,String gender) throws IllegalArgumentException {
        String errorMsg = validateParameters(userName, password, email, university, degree, curriculum, gender);
        if (!errorMsg.isEmpty()) {
            log.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        this.userName = userName;
        this.memberSince = Instant.now().atZone(ZoneId.systemDefault()).format(formatter);
        this.password = password;
        this.email = email;
        this.university = university;
        this.degree = degree;
        this.curriculum = curriculum;
        this.notifications = new ArrayList<>();
        this.sessions = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.gender = gender;
    }
    public String validateParameters(String userName, String password, String email, String university, String degree, String curriculum, String gender) {
        StringBuilder errorMessage = new StringBuilder();
        if (userName == null || userName.isEmpty()) {
            errorMessage.append("Username cannot be empty\n");
        }
        if (password == null || password.length() < 8) {
            errorMessage.append("Invalid password\n");
        }
        if (email == null || !email.contains("@") || !email.contains(".")) {
            errorMessage.append("Invalid email\n");
        }
        if (university == null || university.isEmpty()) {
            errorMessage.append("University cannot be empty\n");
        }
        if (degree == null || degree.isEmpty()) {
            errorMessage.append("Degree cannot be empty\n");
        }
        if (curriculum == null || curriculum.isEmpty()) {
            errorMessage.append("Curriculum cannot be empty\n");
        }
        if(!gender.equals("male") && !gender.equals("female")) {
            errorMessage.append("Gender must be either male or female\n");
        }
        return errorMessage.toString();
    }
    public String getUserName() {
        return userName;
    }
    public String getEncryptedPassword() {
        return password;
    }
    public boolean isInGroup(Group group) {
        return groups.contains(group);
    }
    public void addGroup(Group group) throws IllegalArgumentException{
        if (!groups.contains(group)) {
            groups.add(group);
            if(!group.isMember(this))
                group.addMember(this);
            log.info(String.format("User %s added to group %s", userName, group.getGroupName()));
        }
        else {
            String message = String.format("User %s already in group %s", userName, group.getGroupName());
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
