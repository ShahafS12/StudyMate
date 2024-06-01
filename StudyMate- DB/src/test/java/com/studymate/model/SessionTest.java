package com.studymate.model;

import com.studymate.model.Group;
import com.studymate.model.Session.Session;
import com.studymate.model.User;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest
{
    private final Calendar calendar = Calendar.getInstance();
    @Test
    public void testCreatingSession()
    {
        User user = new User("testUser", "password", "testEmail@gmail.com", "testUniversity", "testDegree", "testCurriculum", "male");
        calendar.set(3038,Calendar.DECEMBER,31,23,59,59);
        Date sessionDate = calendar.getTime();
        Date groupCreatedDate = new Date();
        groupCreatedDate.setYear(2021);
        Group group = new Group("testGroup","testInstitute","testCurriculum" , groupCreatedDate , user,Collections.emptyList());
        Session session = new Session(sessionDate,"MTA Libary", new ArrayList<>(),8,user,true,"regular sesson",group);
        assertEquals(session.getCreatedBy(), user);
        assertEquals(session.getSessionDate(), sessionDate);
    }
    @Test
    public void addExistingUserToSession()
    {
        User user = new User("testUser", "password", "testEmail@gmail.com", "testUniversity", "testDegree", "testCurriculum", "male");
        calendar.set(3038,Calendar.DECEMBER,31,23,59,59);
        Date sessionDate = calendar.getTime();
        Date groupCreatedDate = new Date();
        groupCreatedDate.setYear(2021);
        Group group = new Group("testGroup","testInstitute","testCurriculum" , groupCreatedDate ,user, Collections.emptyList());

        Session session = new Session(sessionDate,"MTA Libary", new ArrayList<>(),8,user,true,"regular sesson",group);
        String expected = String.format("Participant %s is already in session %s", user.getUserName(), session.getId().toString());

        try {
            session.addParticipant(user,user);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            assertEquals(expected,e.getMessage());
        }
    }
    @Test
    public void addParticipantToSession()
    {
        User user = new User("testUser", "password", "testEmail@gmail.com", "testUniversity", "testDegree", "testCurriculum", "male");
        User user2 = new User("testUser2", "password", "testEmail@gmail.com", "testUniversity", "testDegree", "testCurriculum", "male");
        calendar.set(3038,Calendar.DECEMBER,31,23,59,59);
        Date sessionDate = calendar.getTime();
        Date groupCreatedDate = new Date();
        groupCreatedDate.setYear(2021);
        Group group = new Group("testGroup","testInstitute","testCurriculum" , groupCreatedDate ,user, Collections.emptyList());
        group.addMember(user,user2);
        Session session = new Session(sessionDate,"MTA Libary", new ArrayList<>(),8,user,true,"regular sesson",group);
        session.addParticipant(user,user2);
        assertTrue(session.getParticipants().contains(user2));
    }
    @Test
    public void createSessionWithInvalidUser()
    {
        User user = new User("testUser", "password", "testEmail@gmail.com", "testUniversity", "testDegree", "testCurriculum", "male");
        User user2 = new User("testUser2", "password", "testEmail@gmail.com", "testUniversity", "testDegree", "testCurriculum", "male");
        calendar.set(3038,Calendar.DECEMBER,31,23,59,59);
        Date sessionDate = calendar.getTime();
        Date groupCreatedDate = new Date();
        groupCreatedDate.setYear(2020);
        Group group = new Group("testGroup","testInstitute","testCurriculum" , groupCreatedDate ,user, Collections.emptyList());
        String expected = String.format("Participant %s is not part of the group related to the session", user2.getUserName());
        try {
            Session session = new Session(sessionDate,"MTA Libary", new ArrayList<>(),8,user2,true,"regular sesson",group);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            assertEquals(expected, e.getMessage());
        }
    }
    @Test
    public void createSessionInPast()
    {
        User user = new User("testUser", "password", "testEmail@gmail.com", "testUniversity", "testDegree", "testCurriculum", "male");
        calendar.set(2020,Calendar.DECEMBER,31,23,59,59);
        Date sessionDate = calendar.getTime();
        sessionDate.setYear(0);
        Date groupCreatedDate = new Date();
        groupCreatedDate.setYear(2021);
        Group group = new Group("testGroup","testInstitute","testCurriculum" , groupCreatedDate ,user, Collections.emptyList());

        try {
            Session session = new Session(sessionDate,"MTA Libary", new ArrayList<>(),8,user,true,"regular sesson",group);            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            assertEquals("can not set session date to the past", e.getMessage());
        }
    }
    @Test
    public void addParticipantToFullSession()
    {
        User user = new User("testUser", "password", "testEmail@gmail.com@gmail.com", "testUniversity", "testDegree", "testCurriculum", "male");
        User user2 = new User("testUser2", "password", "testEmail@gmail.com@gmail.com", "testUniversity", "testDegree", "testCurriculum", "male");
        calendar.set(3038,Calendar.DECEMBER,31,23,59,59);
        Date sessionDate = calendar.getTime();
        Date groupCreatedDate = new Date();
        groupCreatedDate.setYear(100);
        Group group = new Group("testGroup","testInstitute","testCurriculum" , groupCreatedDate ,user, Collections.emptyList());
        group.addMember(user,user2);
        Session session = new Session(sessionDate,"MTA Libary", new ArrayList<>(),1,user,true,"regular sesson",group);
        try {
            session.addParticipant(user,user2);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Session is full", e.getMessage());
        }
    }

    @Test
    public void removeParticipantFromSession()
    {
        User user = new User("testUser", "password", "testEmail@gmail.com@gmail.com", "testUniversity", "testDegree", "testCurriculum", "male");
        User user2 = new User("testUser2", "password", "testEmail@gmail.com@gmail.com", "testUniversity", "testDegree", "testCurriculum", "male");
        calendar.set(3038,Calendar.DECEMBER,31,23,59,59);
        Date sessionDate = calendar.getTime();
        Date groupCreatedDate = new Date();
        groupCreatedDate.setYear(100);
        Group group = new Group("testGroup","testInstitute","testCurriculum" , groupCreatedDate ,user, Collections.emptyList());
        group.addMember(user,user2);
        Session session = new Session(sessionDate,"MTA Libary", new ArrayList<>(),1,user,false,"regular sesson",group);
        try {
            session.addParticipant(user,user2);
            session.removeParticipantByAdmin(user,user2);
            assertTrue(!session.getParticipants().contains(user2));

        }
        catch (IllegalArgumentException e) {
            fail("Expected IllegalArgumentException");
        }
    }

    @Test
    public void removeLastParticipantFromSession()
    {
        User user = new User("testUser", "password", "testEmail@gmail.com@gmail.com", "testUniversity", "testDegree", "testCurriculum", "male");
        User user2 = new User("testUser2", "password", "testEmail@gmail.com@gmail.com", "testUniversity", "testDegree", "testCurriculum", "male");
        calendar.set(3038,Calendar.DECEMBER,31,23,59,59);
        Date sessionDate = calendar.getTime();
        Date groupCreatedDate = new Date();
        groupCreatedDate.setYear(100);
        Group group = new Group("testGroup","testInstitute","testCurriculum" , groupCreatedDate ,user, Collections.emptyList());
        group.addMember(user,user2);
        Session session = new Session(sessionDate,"MTA Libary", new ArrayList<>(),1,user,false,"regular sesson",group);
        try {
            session.addParticipant(user,user2);
            session.removeParticipantByAdmin(user,user2);
            session.removeParticipantByAdmin(user,user);

            assertTrue(session.isCanceled()==true);

        }
        catch (IllegalArgumentException e) {
            fail("Expected IllegalArgumentException");
        }
    }


}