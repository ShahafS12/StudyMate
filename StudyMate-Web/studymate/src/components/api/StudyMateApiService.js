import axios from "axios";

const STUDYMATE_API_BASE_URL = 'http://localhost:8080/';

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

export const getSessionsForUser = async (username, token) => {
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
        const response = await apiClient.get(`groups/allGroupNames`, {
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