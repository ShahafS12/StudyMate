package com.studymate.dtos;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SessionDto {

    private List<String> membersName;
    private List<String> adminsName;
    private Date createdDate;
    private Date sessionDate;
    private String location;
    private UUID id;
    private String description;

    public SessionDto(List<String> membersName,List<String> adminsName, Date createdDate, Date sessionDate, String location,UUID id
     ,String description ) {
        this.membersName = membersName;
        this.adminsName = adminsName;
        this.createdDate = createdDate;
        this.sessionDate = sessionDate;
        this.location = location;
        this.id = id;
        this.description = description;
    }




}
