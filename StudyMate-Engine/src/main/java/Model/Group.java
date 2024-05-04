package Model;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Group {
    private static final Logger log = LogManager.getLogger(Group.class);
    private String groupName;
    private String institute;
    private String curriculum;
    private Date createdDate;
    private User groupAdmin;
    private List<User> members;
    private List<Session> sessions;

    public static final int MAX_Name_Size = 120;

    public Group(String name, String institute, String curriculum,Date createdDate, User groupAdmin , List<User> members ) {
        if (validateParameters(name, institute, curriculum))
        {
        this.groupName = name;
        this.institute = institute;
        this.curriculum = curriculum;
        this.createdDate=createdDate;
        this.members = new ArrayList<>();
        addMembers(members);
        this.sessions = new ArrayList<>();
        }
    }
    public void addMembers(List<User> members) {
            try {
                 for (User member : members) {
                        addMember(member);
                 }
            log.info("all members added");
        }
        catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            this.members.removeAll(members);
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
        log.info(String.format("User '%s' added to group '%s'", user.getUserName(), groupName));
    }
    public void removeMember(User user) throws IllegalArgumentException {
        if (!isMember(user)) {
            String message = String.format("Cannot remove user. User '%s' is not a member of the group", user.getUserName());
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        members.remove(user);
        log.info(String.format("User '%s' removed from group '%s'", user.getUserName(), groupName));
    }
    public void setGroupName(String groupName) {
        StringBuilder errorMessage = new StringBuilder();
        if (!checkNameValidation(groupName,errorMessage)) {
            log.error("fail to set groupName -" + errorMessage.toString());
            throw new IllegalArgumentException("fail to set groupName -" + errorMessage.toString());
        }
        this.groupName = groupName;
        log.info(String.format("group groupName changed to  '%s' ", groupName));
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
            errorMessage.append("group groupName cannot be empty\n");
            valid=false;
        }
        if (name.length()>MAX_Name_Size) {
            errorMessage.append("group groupName cannot be longer than ")
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
    public boolean checkcreatedDateViolation(Date createdDate, StringBuilder errorMessage){
        boolean valid = true;
        Date currentDate = new Date();
       if (createdDate.after(currentDate)) {
           valid = false;
       }
       return valid;
    }
    public void addSession(Session session) {
        sessions.add(session);
    }
    public String getGroupName() {
        return groupName;
    }
    public boolean isMember(User user) {
        return members.contains(user);
    }
}
