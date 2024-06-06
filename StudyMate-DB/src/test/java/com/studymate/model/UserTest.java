package com.studymate.model;

import com.studymate.model.Session.Session;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private final Calendar calendar = Calendar.getInstance();


    @Test
    public void testCreatingUserSuccess()
    {
        User user = new User("exampleUser", "examplePassword",
                "exampleEmail@gmail.com", "exampleUniversity", "exampleDegree", "exampleCurriculum", "male");
        assertEquals("exampleUser", user.getUserName());
        assertEquals("exampleEmail@gmail.com", user.getEmail());
    }

    @Test
    public void testCreatingUserInvalidUsername() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new User("", "examplePassword", "exampleEmail@gmail.com",
                    "exampleUniversity", "exampleDegree", "exampleCurriculum", "male");
        });
        assertEquals("Username cannot be empty\n", exception.getMessage());
    }

    @Test
    public void testCreatingUserInvalidPassword() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new User("exampleUser", "1234", "exampleEmail@gmail.com",
                    "exampleUniversity", "exampleDegree", "exampleCurriculum", "male");
    });
        assertEquals("Invalid password\n", exception.getMessage());
    }

    @Test
    public void testCreatingUserInvalidEmail() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new User("exampleUser", "examplePassword",
                    "exampleEmailgmail.com", "exampleUniversity", "exampleDegree", "exampleCurriculum", "male");
    });
        assertEquals("Invalid email\n", exception.getMessage());
    }

    @Test
    public void testCreatingUserInvalidUniversity() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new User("exampleUser", "examplePassword1234", "exampleEmail@gmail.com",
                    "", "exampleDegree", "exampleCurriculum", "male");
        });
        assertEquals("University cannot be empty\n", exception.getMessage());
    }

    @Test
    public void testCreatingUserInvalidDegree() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new User("exampleUser", "examplePassword1234", "exampleEmail@gmail.com",
                    "exampleUniversity", "", "exampleCurriculum", "male");
        });
        assertEquals("Degree cannot be empty\n", exception.getMessage());
    }

    @Test
    public void testCreatingUserInvalidCurriculum() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new User("exampleUser", "examplePassword1234", "exampleEmail@gmail.com",
                    "exampleUniversity", "exampleDegree", "", "male");
        });
        assertEquals("Curriculum cannot be empty\n", exception.getMessage());
    }

    @Test
    public void testCreatingUserInvalidGender() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new User("exampleUser", "examplePassword1234", "exampleEmail@gmail.com",
                    "exampleUniversity", "exampleDegree", "exampleCurriculum", "alien");
        });
        assertEquals("Gender must be either male or female\n", exception.getMessage());
    }

    @Test
    public void testGetters() {
        User user = new User("JaneDoe", "password123", "jane.doe@example.com", "MIT", "Physics", "PHY101", "female");
        assertEquals("JaneDoe", user.getUserName());
        assertEquals("jane.doe@example.com", user.getEmail());
        assertEquals("MIT", user.getUniversity());
        assertEquals("Physics", user.getDegree());
        assertEquals("PHY101", user.getCurriculum());
        assertEquals("female", user.getGender());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        assertEquals(Instant.now().atZone(ZoneId.systemDefault()).format(formatter), user.getMemberSince());
    }

    @Test
    public void testDeleteSession() {//todo check if this passes after Amits changes
        User user = new User("JohnDoe", "password123", "john.doe@example.com", "Harvard", "Computer Science", "CS101", "male");
        calendar.set(3038,Calendar.DECEMBER,31,23,59,59);
        Date sessionDate = calendar.getTime();
        Group group = new Group("testGroup","testInstitute","testCurriculum" , new Date() , user, Collections.emptyList());
        Session session = new Session(sessionDate,"MTA Libary", new ArrayList<>(),8,user,true,"regular sesson",group);
        user.addSession(session);
        user.deleteSession(session);
        assertFalse(user.getSessions().contains(session));
    }

    @Test
    public void testAddGroup() {//todo check if this passes after Amits changes
        User user = new User("JohnDoe", "password123", "john.doe@example.com", "Harvard", "Computer Science", "CS101", "male");
        User user2 = new User("JohnDoe2", "password123", "john.doe2@example.com", "Harvard", "Computer Science", "CS101", "male");
        Group group = new Group("testGroup", "testInstitute", "testCurriculum", new Date(), user, Collections.emptyList());
        user2.addGroup(group);
        assertTrue(user2.isInGroup(group));
    }

    @Test
    public void testAddGroupInvalidUser() {//todo check if this passes after Amits changes
        User user = new User("JohnDoe", "password123", "john.doe@example.com", "Harvard", "Computer Science", "CS101", "male");
        Group group = new Group("testGroup", "testInstitute", "testCurriculum", new Date(), user, Collections.emptyList());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            user.addGroup(group);
        });
        assertEquals("User JohnDoe is not in group testGroup\n", exception.getMessage());
    }

}