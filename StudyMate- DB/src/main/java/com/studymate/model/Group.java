package com.studymate.model;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.studymate.model.Session.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Group {
    private static final Logger log = LogManager.getLogger(Group.class);
    private String groupName;
    private String institute;
    private String curriculum;
    private Date createdDate;
    private User groupCreator;
    private List<User> groupAdmins;
    private List<User> members;
    private List<Session> sessions;
    private boolean isDeleted =false;
    public static final int MAX_Name_Size = 120;

    public Group(String name, String institute, String curriculum,Date createdDate, User groupCreator , List<User> members ) {
        if (validateParameters(name, institute, curriculum))
        {
        this.members = new ArrayList<>();
        this.sessions = new ArrayList<>();
        this.groupAdmins=new ArrayList<>();
        this.groupName = name;
        this.institute = institute;
        this.curriculum = curriculum;
        this.createdDate=createdDate;
        this.groupCreator=groupCreator;
        if (members.size()!=0)
        {
        members.add(groupCreator);
        }
        else
        {
        members=new ArrayList<>();
        members.add(groupCreator);
        }
        groupAdmins.add(groupCreator);
        addMembers(groupCreator,members);

        }
    }
    public void addMembers(User adminCandidate, List<User> members) {
        try
        {
             for (User member : members)
             {
                 addMember(adminCandidate,member);
             }
            log.info("all members added");
        }
        catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            this.members.removeAll(members);
        }
    }
    public void addMember(User adminCandidate,User user) throws IllegalArgumentException {
        checkIfUserIsAdmin(adminCandidate);
        if (isMember(user)) {
            String message = String.format("User '%s' is already a member of the group", user.getUserName());
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        members.add(user);
        user.addGroup(this);
        log.info(String.format("User '%s' added to group '%s'", user.getUserName(), groupName));
    }
    public void removeMemberByAdmin(User adminCandidate, User user) throws IllegalArgumentException {
        checkIfUserIsAdmin(adminCandidate);
        if (!isMember(user)) {
            String message = String.format("Cannot remove user. User '%s' is not a member of the group", user.getUserName());
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        removeMember(user);
    }
    public void removeMember( User user) throws IllegalArgumentException {
     checkIfUserIsMember(user);
     if (groupAdmins.contains(user) )
     {
        removeMemberFromBeingAdmin(user);
     }
     members.remove(user);
     log.info(String.format("User '%s' removed from group '%s'", user.getUserName(), groupName));
     if (members.size()==0)
     {
        log.info(String.format("members size is 0 deleting group %s", groupName));
        deleteGroup();
     }
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
    public void checkIfUserIsAdmin(User managerCandidate) throws IllegalArgumentException{
        if (! groupAdmins.contains(managerCandidate)){
              String message = String.format("USer %s is not in an admin of the group", managerCandidate.getUserName());
                log.error(message);
                throw new IllegalArgumentException(message);
            }
    }
    public void checkIfUserIsMember(User participantCandidate) throws IllegalArgumentException{
        if (! members.contains(participantCandidate)){
              String message = String.format("user %s is not in a member of the group", participantCandidate.getUserName());
                log.error(message);
                throw new IllegalArgumentException(message);
            }
    }
    public void addSessionToGroup(Session session) {
        sessions.add(session);
    }
    public String getGroupName() {
        return groupName;
    }
    public boolean isMember(User user) {
        return members.contains(user);
    }
    //create getters
    public String getInstitute() {
        return institute;
    }
    public String getCurriculum() {
        return curriculum;
    }
    public Date getCreatedDate() {
        return createdDate;
    }
    public List<User> getGroupAdmin() {
        return groupAdmins;
    }
    public List<User> getMembers() {
        return members;
    }
    public List<Session> getSessions() {
        return sessions;
    }
    public List<String> getMembersNames() {
        List<String> membersNames = new ArrayList<>();
        for (User member : members) {
            membersNames.add(member.getUserName());
        }
        return membersNames;
    }
    public List<String> getAdminsNames() {
        List<String> AdminNames = new ArrayList<>();
        for (User admin : groupAdmins) {
            AdminNames.add(admin.getUserName());
        }
        return AdminNames;
    }
    public void setMemberAsAdmin(User adminCandidate,User member) throws IllegalArgumentException {
       log.info(String.format("Member %s trying to set User %s as admin of Group %s"
               ,adminCandidate.getUserName() ,member.getUserName(),groupName));
       checkIfUserIsMember(member);
       checkIfUserIsAdmin(adminCandidate);
        groupAdmins.add(member);
        log.info(String.format("user %s is now admin of group %s"
                ,member.getUserName(),groupName));
    }
    public void removeMemberFromBeingAdminByAdmin(User adminCandidate,User member)throws IllegalArgumentException {
        checkIfUserIsAdmin(adminCandidate);
        removeMemberFromBeingAdmin(member);
    }

    public void removeMemberFromBeingAdmin(User admin)throws IllegalArgumentException {
        checkIfUserIsAdmin(admin);
        if ( groupAdmins.size()==1){ // the only admin want to not be admin of Group
            User nextAdmin = null;
            int i=0;
            boolean found=false;
            while (i<members.size() && !found) // look for the next participant to be admin
            {
                nextAdmin=members.get(i);
                if (nextAdmin!=admin)
                {
                    found=true;
                }
                i++;
            }
            if (found)
            {
                setMemberAsAdmin(admin,nextAdmin);
            }
        }
        groupAdmins.remove(admin);
        log.info(String.format("member %s removed from being admin in group %s "
                ,admin.getUserName(),groupName));
    }
    public void deleteGroup() {
        isDeleted = true;
         log.info(String.format("deleting group %s",groupName));
    }
    public void deleteGroupByAdmin(User managerCandidate ){
        checkIfUserIsAdmin(managerCandidate);
        int size=members.size();
        for (int i=0;i<size;i++)
        {
            User member=members.get(0);
            removeMember(member);
        }
    }

}
