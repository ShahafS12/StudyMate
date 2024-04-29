package Model;
import java.util.ArrayList;
import java.util.List;

import Model.Session;
import Model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Group {
    private static final Logger log = LogManager.getLogger(Group.class);
    private String name;
    private String institute;
    private String curriculum;
    private List<User> members;
    private List<Session> sessions;

    public static final int MAX_Name_Size = 120;

    public Group(String name, String institute, String curriculum,List<User> members) {
        if (validateParameters(name, institute, curriculum))
        {
        this.name = name;
        this.institute = institute;
        this.curriculum = curriculum;
        this.members = members;
        this.sessions = new ArrayList<>();
        }
    }

    public void addMember(User user) throws IllegalArgumentException {
        if (isMember(user)) {
            String message = String.format("User '%s' is already a member of the group", user.getUserName());
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        members.add(user);
        user.addGroup(this);
        log.info(String.format("User '%s' added to group '%s'", user.getUserName(), name));
    }
    public void removeMember(User user) throws IllegalArgumentException {
        if (!isMember(user)) {
            String message = String.format("Cannot remove user. User '%s' is not a member of the group", user.getUserName());
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        members.remove(user);
        log.info(String.format("User '%s' removed from group '%s'", user.getUserName(), name));
    }
    public void setName(String name) {
        StringBuilder errorMessage = new StringBuilder();
        if (!checkNameValidation(name,errorMessage)) {
            log.error("fail to set name -" + errorMessage.toString());
            throw new IllegalArgumentException("fail to set name -" + errorMessage.toString());
        }
        this.name = name;
        log.info(String.format("group name changed to  '%s' ", name));
    }
    public boolean validateParameters(String name, String university, String curriculum) {
        StringBuilder errorMessage = new StringBuilder();
        boolean groupNameValidation= checkNameValidation(name,errorMessage);
        boolean universityValidation= checkInstitueValidation(university,errorMessage);
        boolean curriculumValidation= checkCurriculumValidation(curriculum,errorMessage);

        return (groupNameValidation&&universityValidation && curriculumValidation);
    }
    public boolean checkNameValidation(String name, StringBuilder errorMessage){
        boolean valid = true;
        if (name == null || name.isEmpty()) {
            errorMessage.append("group name cannot be empty\n");
            valid=false;
        }
        if (name.length()>MAX_Name_Size) {
            errorMessage.append("group name cannot be longer than ")
             .append(MAX_Name_Size)
             .append(" size \n");
            valid=false;
        }
        return valid;
    }
    public boolean checkInstitueValidation(String name, StringBuilder errorMessage){
        boolean valid = true;
       // NEED to think if we want to have a list of institutions
        return valid;
    }
     public boolean checkCurriculumValidation(String name, StringBuilder errorMessage){
        boolean valid = true;
       // NEED to think if we want to have a list of Curriculum
        return valid;
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
