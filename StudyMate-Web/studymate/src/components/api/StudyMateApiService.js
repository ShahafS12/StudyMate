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

export const getUser = async (username) => {
    try {
        const response = await apiClient.get(`/users/${username}`);
        return response.data;
    } catch (error) {
        console.error('Get user error:', error);
        throw error;
    }
}