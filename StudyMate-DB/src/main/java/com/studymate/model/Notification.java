package com.studymate.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.net.URL;
import java.util.Date;

@Document(collection = "notifications")
public class Notification
{
    private static final Logger log = LogManager.getLogger(Group.class);
    @Id
    private String id;
    private final String message;
    private final User user;
    private final URL url;
    private final Date createdDate;

    public Notification(String message, User user, URL url)
    {
        this.message = message;
        this.user = user;
        this.url = url;
        this.createdDate = new Date();
    }
    //getters
    public String getMessage()
    {
        return message;
    }
    public User getUser()
    {
        return user;
    }
    public URL getUrl()
    {
        return url;
    }
    public Date getCreatedDate()
    {
        return createdDate;
    }
}
