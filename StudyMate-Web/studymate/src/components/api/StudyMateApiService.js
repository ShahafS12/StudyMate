import axios from "axios";

const STUDYMATE_API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080/';

const apiClient= axios.create(
    {
        baseURL: STUDYMATE_API_BASE_URL
    }
)

export const getErrorCode = async () => {
    await apiClient.get(`/errorCode`);
}

export const registerUser = async (username, password, email, university, degree, curriculum, gender) => {
    try {
        const userData = { username, password, email, university, degree, curriculum, gender };
        const response = await apiClient.post(`/users/createUser`, JSON.stringify(userData),
            {headers: { 'Content-Type': 'application/json' }
        });
        return response.data;
    } catch (error) {
        console.error('Registration error:', error);
        throw error;
    }
}

export const loginUser = async (username, password) => {
    try {
        const userData = { username, password };
        const response = await apiClient.post(`/auth/login`, JSON.stringify(userData),
            {headers: { 'Content-Type': 'application/json' }
            });
        return response.data;
    } catch (error) {
        console.error('Login error:', error);
        throw error;
    }
}

export const getUser = async (username, token) => {
    try {
        const response = await apiClient.get(`users/getUser/${username}`, {
            headers: {
                'Authorization': `Bearer ${token}` // Include the JWT token in the Authorization header
            }
        });
        return response.data;
    } catch (error) {
        console.error('Get user error:', error);
        throw error;
    }
}

export const getGroupsForUser = async (username, token) => {
    try {
        const response = await apiClient.get(`users/getUserGroups/${username}`, {
            headers: {
                'Authorization': `Bearer ${token}` // Include the JWT token in the Authorization header
            }
        });
        return response.data;
    } catch (error) {
        console.error('Get groups error:', error);
        throw error;
    }
}

export const getSessionsForUser = async (token, username) => {
    try {
        const response = await apiClient.get(`users/getUserSessions/${username}`, {
            headers: {
                'Authorization': `Bearer ${token}` // Include the JWT token in the Authorization header
            }
        });
        return response.data;
    }
    catch (error) {
        console.error('Get sessions error:', error);
        throw error;
    }
}

export const getUserNotifications = async (username, token) => {
    try {
        const response = await apiClient.get(`users/getUserNotifications/${username}`, {
            headers: {
                'Authorization': `Bearer ${token}` // Include the JWT token in the Authorization header
            }
        });
        return response.data;
    }
    catch (error) {
        console.error('Get notifications error:', error);
        throw error;
    }
}

export const getGroups = async (token) => {
    try {
        const response = await apiClient.get(`group/allGroupNames`, {
            headers: {
          //      'Authorization': `Bearer ${token}` // Include the JWT token in the Authorization header
            }
        });
        return response.data;
    }
    catch (error) {
        console.error('Get groups error:', error);
        throw error;
    }
}

export const createGroup = async (group, token) => {
    try {
        const response = await apiClient.post(`group/createGroup`, JSON.stringify(group), {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });
        return response.data;
    }
    catch (error) {
        console.error('Create group error:', error);
        throw error;
    }
}

export const getGroupByName = async (name) => {
    try {
        const decodedName = decodeURIComponent(name);
        const response = await apiClient.get(`group/getGroup/${decodedName}`);
        return response.data;
    }
    catch (error) {
        console.error('Get group by name' + name, error);
        throw error;
    }
}


export const createSession = async (session, token) => {
    try {
        console.log(session);
        const response = await apiClient.post('/session/createSession', session, {
            headers: {
                Authorization: `Bearer ${token}`, // Send the token in the Authorization header
                'Content-Type': 'application/json'
            }
        });
        return response.data;
    } catch (error) {
        console.error('Create session error:', error);
        throw error;
    }
}


export const isUserMemberInGroup = async (userName, groupName) => {
    let isMember = false;
    try {
        const response = await apiClient.get(`users/isInGroup/${userName}/${groupName}`);
        isMember = response.data;
    }
    catch (error){
        console.error('');
        throw error;
    }
    finally {
        return isMember;
    }
}

export const getSessionsForGroup = async (groupName) => {
    try {
        const response = await apiClient.get(`group/getGroupSessions/${groupName}`, {});
        return response.data;
    }
    catch (error) {
        console.error('Get sessions error:', error);
        throw error;
    }
}

export const exitSession = async (token, sessionId) => {
    try {
        const response = await apiClient.post(`session/removeMyselfFromSession/${sessionId}"`, {});
        return response.data;
    }
    catch (error) {
        console.error('Get sessions error:', error);
        throw error;
    }
}

export const joinSession = async (token, sessionId) => {
    try {
        const response = await apiClient.post(`session/addMyselfToSession/${sessionId}`, {});
        return response.data;
    }
    catch (error) {
        console.error('Get sessions error:', error);
        throw error;
    }
}

export const joinGroup = async (token, groupName, userName) => {
    try {
        const response = await apiClient.post(
            `group/addUserToGroup/${groupName}/${userName}`,
            {}, // An empty body if your endpoint doesn't expect a request payload
            {
                headers: {
                    'Authorization': `Bearer ${token}` // Include the JWT token in the Authorization header
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error('Error joining group:', error);
        throw error;
    }
};

export const exitGroup = async (token, groupName, userName) => {
    try {
        const response = await apiClient.post(
            `group/removeUserFromGroup/${groupName}/${userName}`,
            {}, // An empty body if your endpoint doesn't expect a request payload
            {
                headers: {
                    'Authorization': `Bearer ${token}` // Include the JWT token in the Authorization header
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error('Error exiting group:', error);
        throw error;
    }
};

export const search = async (queryWord) => {
    try {
        const response = await apiClient.get(`/search?query=${queryWord}`);
        return response.data;
    }
    catch (error) {
        console.error('Get sessions error:', error);
        throw error;
    }
}