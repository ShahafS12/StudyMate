package com.studymate.apis.constansts;

public class URLMappingConstants
{
    // User
    public static final String USERS = "/users";
    public static final String CREATE_USER = "/createUser";
    public static final String ALL_USER_NAMES = "/allUserNames";
    public static final String GET_USER = "/getUser/{username}";
    public static final String GET_USER_NOTIFICATIONS = "/getUserNotifications/{username}";
    public static final String GET_USER_GROUPS = "/getUserGroups/{username}";
    public static final String GET_USER_SESSIONS = "/getUserSessions/{username}";
    public static final String IS_IN_GROUP = "/isInGroup/{username}/{groupName}";
    // Group
    public static final String GROUP = "/group";
    public static final String CREATE_GROUP = "/createGroup";
    public static final String ALL_GROUP_NAMES = "/allGroupNames";
    public static final String GET_GROUP = "/getGroup/{groupName}";
    public static final String ADD_USER_TO_GROUP = "/addUserToGroup/{groupName}/{username}";
    public static final String REMOVE_USER_FROM_GROUP = "/removeUserFromGroup/{groupName}/{username}";
    public static final String SET_USER_AS_ADMIN_OF_GROUP = "/setUserAsAdmin/{groupName}/{username}";
    public static final String REMOVE_USER_AS_ADMIN_OF_GROUP = "/removeUserFromAdmin/{groupName}/{username}";
    public static final String DELETE_GROUP_BY_ADMIN = "/deleteGroupByAdmin/{groupName}";
    public static final String GET_GROUP_MEMBERS = "/getGroupMembers/{groupName}";
    public static final String GET_GROUP_SESSIONS = "/getGroupSessions/{groupName}";


    //Session
    public static final String SESSION = "/session";
    public static final String CREATE_SESSION = "/createSession";
    public static final String GET_SESSION = "/getSession/{sessionId}";
    public static final String ADD_USER_TO_SESSION = "/addUserToSession/{sessionId}/{username}";
    public static final String USER_ADD_HIMSELF_TO_SESSION = "/addMyselfToSession/{sessionId}";
    public static final String REMOVE_USER_FROM_SESSION = "/removeUserFromSession/{sessionId}/{username}";
    public static final String USER_REMOVE_HIMSELF_FROM_SESSION = "/removeMyselfFromSession/{sessionId}";
    public static final String SET_USER_AS_ADMIN_OF_SESSION = "/setUserAsAdminOfSession/{sessionId}/{username}";
    public static final String REMOVE_USER_FROM_ADMIN_OF_SESSION = "/removeUserAsAdminOfSession/{sessionId}/{username}";
    public static final String DELETE_SESSION_BY_ADMIN = "/deleteSessionByAdmin/{sessionId}";
    public static final String SET_MAX_PARTICIPANTS_FOR_SESSION = "/setMaxParticipantsForSession/{sessionId}/{isLimited}/{maxParticipants}";


        //Authentications
    public static final String AUTHENTICATE = "/auth";
    public static final String LOGIN = "/login";
    public static final String VALIDATE_TOKEN = "/validate";

    //Search bar
    public static final String SEARCH_BAR = "/search";


}
