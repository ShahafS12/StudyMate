package Model;

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest
{
    private final Calendar calendar = Calendar.getInstance();
    @Test
    public void testCreatingSession()
    {
        User user = new User("testUser", "password");
        calendar.set(3038,Calendar.DECEMBER,31,23,59,59);
        Date sessionDate = calendar.getTime();
        Group group = new Group("testGroup","testInstitute","testCurriculum");
        user.addGroup(group);
        Session session = new Session(sessionDate,user,group);
        assertEquals(session.getCreatedBy(), user);
        assertEquals(session.getSessionDate(), sessionDate);
    }
    @Test
    public void addExistingUserToSession()
    {
        User user = new User("testUser", "password");
        calendar.set(3038,Calendar.DECEMBER,31,23,59,59);
        Date sessionDate = calendar.getTime();
        Group group = new Group("testGroup","testInstitute","testCurriculum");
        group.addMember(user);
        Session session = new Session(sessionDate,user,group);
        try {
            session.addParticipant(user);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Participant testUser is already in session", e.getMessage());
        }
    }
    @Test
    public void addParticipantToSession()
    {
        User user = new User("testUser", "password");
        User user2 = new User("testUser2", "password");
        calendar.set(3038,Calendar.DECEMBER,31,23,59,59);
        Date sessionDate = calendar.getTime();
        Group group = new Group("testGroup", "testInstitute", "testCurriculum");
        group.addMember(user);
        group.addMember(user2);
        Session session = new Session(sessionDate, user, group);
        session.addParticipant(user2);
        assertTrue(session.getParticipants().contains(user2));
    }
    @Test
    public void createSessionWithInvalidUser()
    {
        User user = new User("testUser", "password");
        calendar.set(3038,Calendar.DECEMBER,31,23,59,59);
        Date sessionDate = calendar.getTime();
        Group group = new Group("testGroup", "testInstitute", "testCurriculum");
        try {
            Session session = new Session(sessionDate, user, group);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            assertEquals("User testUser is not in group testGroup\n", e.getMessage());
        }
    }
    @Test
    public void createSessionInPast()
    {
        User user = new User("testUser", "password");
        calendar.set(2020,Calendar.DECEMBER,31,23,59,59);
        Date sessionDate = calendar.getTime();
        Group group = new Group("testGroup", "testInstitute", "testCurriculum");
        group.addMember(user);
        try {
            Session session = new Session(sessionDate, user, group);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Session date is in the past\n", e.getMessage());
        }
    }
    @Test
    public void addParticipantToFullSession()
    {
        User user = new User("testUser", "password");
        User user2 = new User("testUser2", "password");
        calendar.set(3038,Calendar.DECEMBER,31,23,59,59);
        Date sessionDate = calendar.getTime();
        Group group = new Group("testGroup", "testInstitute", "testCurriculum");
        group.addMember(user);
        group.addMember(user2);
        Session session = new Session(sessionDate, user, group,1);
        try {
            session.addParticipant(user2);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Session is full", e.getMessage());
        }
    }
}