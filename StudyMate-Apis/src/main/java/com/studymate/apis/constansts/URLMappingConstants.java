package com.studymate.apis.constansts;

public class URLMappingConstants
{
    // User
    public static final String USERS = "/users";
    public static final String CREATE_USER = "/createUser";
    public static final String CREATE_GROUP = "/createGroup";
    public static final String ALL_USER_NAMES = "/allUserNames";
    public static final String GET_USER = "/getUser/{username}";
    public static final String GET_USER_NOTIFICATIONS = "/getUserNotifications/{username}";
    public static final String GET_USER_GROUPS = "/getUserGroups/{username}";
    public static final String GET_USER_SESSIONS = "/getUserSessions/{username}";
    // Group
    public static final String Group = "/group";
    public static final String ALL_Group_NAMES = "/allGroupNames";
    public static final String GET_Group = "/getGroup/{groupName}";
    public static final String GET_Group_SESSIONS = "/getGroupSessions/{groupName}";
    public static final String GET_All_Users_In_Group = "/getAllUsersInGroup/{groupName}";

    //Authentications
    public static final String AUTHENTICATE = "/auth";
    public static final String LOGIN = "/login";
    public static final String VALIDATE_TOKEN = "/validate";

}
