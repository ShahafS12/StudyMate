package com.studymate.model;

import com.studymate.model.Session.Session;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GroupTest {
     private final Calendar calendar = Calendar.getInstance();


    @Test
    public void testCreatingGroupSuccess()
    {
        User user = new User("exampleUser", "examplePassword",
                "exampleEmail@gmail.com", "exampleUniversity", "exampleDegree", "exampleCurriculum", "male");
        Date sessionDate = calendar.getTime();
        Date groupCreatedDate = new Date();
        groupCreatedDate.setYear(2021);
        Group group = new Group("testGroup","testInstitute","testCurriculum" , groupCreatedDate , user,Collections.emptyList());
        assertEquals("testGroup", group.getGroupName());
    }

    @Test
    public void testDeleteSession() {
        User user = new User("JohnDoe", "password123", "john.doe@example.com", "Harvard", "Computer Science", "CS101", "male");
        User user2 = new User("testUser2", "password", "testEmail@gmail.com", "testUniversity", "testDegree", "testCurriculum", "male");
        calendar.set(3038,Calendar.DECEMBER,31,23,59,59);
        Date sessionDate = calendar.getTime();
        Group group = new Group("testGroup","testInstitute","testCurriculum" , new Date() , user, Collections.emptyList());
        group.addMember(user,user2);
        Session session = new Session(sessionDate,"MTA Libary", new ArrayList<>(),8,user,true,"regular sesson",group);
        session.addParticipant(user,user2);
        session.cancelMeetingByAdmin(user);
        assertFalse(user.getSessions().contains(session));
    }

    @Test
    public void testDeleteGroup() {//
        User user = new User("JohnDoe", "password123", "john.doe@example.com", "Harvard", "Computer Science", "CS101", "male");
        User user2 = new User("JohnDoe2", "password123", "john.doe2@example.com", "Harvard", "Computer Science", "CS101", "male");
        Group group = new Group("testGroup", "testInstitute", "testCurriculum", new Date(), user, Collections.emptyList());
        group.addMember(user,user2);
        group.deleteGroupByAdmin(user);
        assertTrue(user2.isInGroup(group));
    }

    @Test
    public void testAddGroupInvalidUser() {
        User user = new User("JohnDoe", "password123", "john.doe@example.com", "Harvard", "Computer Science", "CS101", "male");
        Group group = new Group("testGroup", "testInstitute", "testCurriculum", new Date(), user, Collections.emptyList());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            user.addGroup(group);
        });
        assertEquals("User JohnDoe is not in group testGroup\n", exception.getMessage());
    }


}
