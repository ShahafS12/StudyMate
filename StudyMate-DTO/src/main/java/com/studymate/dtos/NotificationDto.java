package com.studymate.dtos;

public class NotificationDto {
    private String message;
    private String user;
    private String url;
    private String createdDate;

    public NotificationDto(String message, String user, String url, String createdDate) {
        this.message = message;
        this.user = user;
        this.url = url;
        this.createdDate = createdDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
