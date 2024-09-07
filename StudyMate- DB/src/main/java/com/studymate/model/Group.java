package com.studymate.model;

import com.studymate.model.Session.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Document(collection = "groups")
public class Group {
    private static final Logger log = LogManager.getLogger(Group.class);
     @Id
    ObjectId id;
    private String groupName;
    private String institute;
    private String curriculum;
    private Date createdDate;
    private User groupCreator;
    @DBRef
    private List<User> groupAdmins;
    @DBRef
    private List<User> members ;
    @DBRef
    private List<Session> sessions;

    public boolean isDeleted() {
        return isDeleted;
    }

    private boolean isDeleted =false;
    public static final int MAX_Name_Size = 120;

    public Group(String groupName, String institute, String curriculum,Date createdDate, User groupCreator , List<User> members ) {
        if (validateParameters(groupName, institute, curriculum))
        {
            id=new ObjectId();
            this.members = new ArrayList<>();
            this.sessions = new ArrayList<>();
            this.groupAdmins=new ArrayList<>();
            this.groupName = groupName;
            this.institute = institute;
            this.curriculum = curriculum;
            this.createdDate=createdDate;
            this.groupCreator=groupCreator;
            if (members==null)
            {
                members=new ArrayList<>();
            }
            this.members.add(groupCreator);
            groupCreator.addGroup(this);
            groupAdmins.add(groupCreator);
            addMembers(members);

        }
    }

     public Group(){}
    public void addMembers(List<User> members) {
        try
        {
             for (User member : members)
             {

                 {
                    if (!this.members.contains(member))
                    {
                        this.members.add(member);
                        member.addGroup(this);
                        log.info(String.format("member %s added", member.getUserName()));
                    }
                    else
                    {
                        log.info(String.format("could not add user %s , he is already member" , member.getUserName()));
                    }
                 }

             }
            log.info("all members added");
        }
        catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            this.members.removeAll(members);
        }
    }
    public void addMember(User adminCandidate,User user) throws IllegalArgumentException {
        if (checkIfUserIsAdmin(adminCandidate)) {
            if (checkIfUserIsMember(user)) {
                String message = String.format("User '%s' is already a member of the group", user.getUserName());
                log.error(message);
                throw new IllegalArgumentException(message);
            }
            members.add(user);
            user.addGroup(this);
            log.info(String.format("User '%s' added to group '%s'", user.getUserName(), groupName));
        }
        else
        {
           String message = String.format("User '%s' is not a admin of the group. cant let him add someone for group", adminCandidate.getUserName());
           log.error(message);
           throw new IllegalArgumentException(message);
        }
        }
    public void removeMemberByAdmin(User adminCandidate, User user) throws IllegalArgumentException {
        if (checkIfUserIsAdmin(adminCandidate))
        {
        removeMember(user);
        }
        else
        {
            String message = String.format("User '%s' is not a admin of the group. cant let him remove someone for group", adminCandidate.getUserName());
           log.error(message);
           throw new IllegalArgumentException(message);
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group otherGroup = (Group) o;
        return Objects.equals(this.groupName, otherGroup.groupName);
    }
    public void removeMember( User user) throws IllegalArgumentException {
     if (checkIfUserIsMember(user)) {
         if (checkIfUserIsAdmin(user)) {
             removeMemberFromBeingAdmin(user);
         }
         members.remove(user);
         user.deleteGroup(this);
         log.info(String.format("User '%s' removed from group '%s'", user.getUserName(), groupName));
         if (members.isEmpty())
         {
            log.info(String.format("members size is 0 deleting group %s", groupName));
            deleteGroup();
        }
     }
     else {
           String message = String.format("Cannot remove user. User '%s' is not a member of the group", user.getUserName());
            log.error(message);
            throw new IllegalArgumentException(message);
     }
    }
    public void setGroupName(String groupName) {
        StringBuilder errorMessage = new StringBuilder();
        if (!checkNameValidation(groupName,errorMessage)) {
            log.error("fail to set groupName -" + errorMessage);
            throw new IllegalArgumentException("fail to set groupName -" + errorMessage);
        }
        this.groupName = groupName;
        log.info(String.format("group groupName changed to  '%s' ", groupName));
    }
    public boolean validateParameters(String name, String university, String curriculum) {
        StringBuilder errorMessage = new StringBuilder();
        boolean groupNameValidation= checkNameValidation(name,errorMessage);
        boolean universityValidation= checkInstituteValidation(university,errorMessage);
        boolean curriculumValidation= checkCurriculumValidation(curriculum,errorMessage);
        return (groupNameValidation&&universityValidation && curriculumValidation);
    }
    public boolean checkNameValidation(String name, StringBuilder errorMessage){
        boolean valid = true;
        if (name == null || name.isEmpty()) {
            errorMessage.append("group groupName cannot be empty\n");
            valid=false;
        }
        else if (name.length()>MAX_Name_Size) {
            errorMessage.append("group groupName cannot be longer than ")
             .append(MAX_Name_Size)
             .append(" size \n");
            valid=false;
        }
        return valid;
    }
    public boolean checkInstituteValidation(String name, StringBuilder errorMessage){
        boolean valid = true;
       // NEED to think if we want to have a list of institutions
        return valid;
    }
    public boolean checkCurriculumValidation(String name, StringBuilder errorMessage){
        boolean valid = true;
       // NEED to think if we want to have a list of Curriculum
        return valid;
    }
    private boolean checkIfUsersAreEqual(User one, User two){
        boolean res=false;
        if (one.id().toString().equals(two.id().toString()))
        {
                res=true;
            }
        return res;
    }
    public boolean checkIfUserIsAdmin(User managerCandidate) {
        boolean found = false;
        for (User manager : this.groupAdmins) {
            if (checkIfUsersAreEqual(manager, managerCandidate)) {
                found = true;
            }
        }
        return found;
        }
    public boolean checkIfUserIsMember(User memberCandidate) {
             boolean found = false;
        for (User user : this.members) {
            if (checkIfUsersAreEqual(user, memberCandidate)) {
                found = true;
            }
        }
        return found;
    }
    public void addSessionToGroup(Session session) {
        sessions.add(session);
    }
    public String getGroupName() {
        return groupName;
    }
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
       if (checkIfUserIsMember(member) && checkIfUserIsAdmin(adminCandidate))
        {
        groupAdmins.add(member);
        log.info(String.format("user %s is now admin of group %s"
                ,member.getUserName(),groupName));
        }
        else
       {
           if (!checkIfUserIsMember(member))
           {
               String message = String.format("User '%s' is not a member of the group. cant set him as admin", member.getUserName());
               log.error(message);
               throw new IllegalArgumentException(message);
           }
           if (!checkIfUserIsAdmin(adminCandidate))
           {
               String message = String.format("User '%s' is not a admin of the group. cant let him set someone as admin", adminCandidate.getUserName());
               log.error(message);
               throw new IllegalArgumentException(message);
           }
       }
    }
    public void removeMemberFromBeingAdminByAdmin(User adminCandidate,User member)throws IllegalArgumentException {
        if (checkIfUserIsAdmin(adminCandidate))
        {
            removeMemberFromBeingAdmin(member);
        }
        else
            {
               String message = String.format("User '%s' is not a admin of the group. cant let him remove someone from being admin", adminCandidate.getUserName());
               log.error(message);
               throw new IllegalArgumentException(message);
           }

    }
    public void removeMemberFromBeingAdmin(User admin)throws IllegalArgumentException {
        if (checkIfUserIsAdmin(admin)) {
            if (groupAdmins.size() == 1) { // the only admin want to not be admin of Group
                User nextAdmin = null;
                int i = 0;
                boolean found = false;
                while (i < members.size() && !found) // look for the next participant to be admin
                {
                    nextAdmin = members.get(i);
                    if (nextAdmin != admin) {
                        found = true;
                    }
                    i++;
                }
                if (found) {
                    setMemberAsAdmin(admin, nextAdmin);
                }
            }
            groupAdmins.remove(admin);
            log.info(String.format("member %s removed from being admin in group %s "
                    , admin.getUserName(), groupName));
        }
        else
        {
            {
               String message = String.format("User '%s' is not a admin of the group. cant remove him from being admin", admin.getUserName());
               log.error(message);
               throw new IllegalArgumentException(message);
           }
        }
        }
    public void deleteGroup() {
        isDeleted = true;
         log.info(String.format("deleting group %s",groupName));
    }
    public void removeAllMembersByAdmin(User managerCandidate ) {
        if (checkIfUserIsAdmin(managerCandidate)) {
            int size = members.size();
            for (int i = 0; i < size; i++) {
                User member = members.get(0);
                removeMember(member);
            }
        }
        else
        {
           String message = String.format("User '%s' is not a admin of the group. cant delete group", managerCandidate.getUserName());
           log.error(message);
           throw new IllegalArgumentException(message);
        }
    }
    public void removeSession(Session session){
        if (sessions.contains(session)){
            int numberOfParticipants=session.getParticipants().size();
            for (int i=0;i<numberOfParticipants;i++)
            {
                User participant=session.getParticipants().get(0);
                removeUserFromSession(participant,session);
            }
            log.info(String.format("removing session %s from group %s ",session.getSessionId(),groupName));
            sessions.remove(session);
        }
        else {
            log.info("session not found in group");
        }
    }
    public void removeUserFromSession(User user, Session session){
        if (session.getParticipants().contains(user)){
            log.info(String.format("removing user %s from session %s from group %s ",user.getUserName(),session.getSessionId(),groupName));
            session.removeParticipant(user);
        }
        else {
            log.info(String.format(" user %s in not in session %s",user.getUserName(),session.getSessionId()));
        }
    }
}

