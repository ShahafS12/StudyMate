package com.studymate.dtos;

import java.util.List;

public class UserDto {
    private String username;
    private String password;
    private String email;
    private String university;
    private String degree;
    private String curriculum;
    private String gender;
    private String memberSince;
    public UserDto() {
    }
    public UserDto(String username, String password, String email, String university, String degree, String curriculum, String gender,
                   String memberSince) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.university = university;
        this.degree = degree;
        this.curriculum = curriculum;
        this.gender = gender;
        this.memberSince = memberSince;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(String curriculum) {
        this.curriculum = curriculum;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getMemberSince() {
        return memberSince;
    }
    public void setMemberSince(String memberSince) {
        this.memberSince = memberSince;
    }
}
