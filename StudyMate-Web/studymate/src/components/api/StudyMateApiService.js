import axios from "axios";

const STUDYMATE_API_BASE_URL = 'http://localhost:8080/v1';

const apiClient= axios.create(
    {
        baseURL: STUDYMATE_API_BASE_URL
    }
)

export const registerUser = async (username, password, email, university, degree,type, gender) => {
    try {
        const response = await apiClient.post(`/users/create?username=${username}&password=${password}&email=${email}&university=${university}
        &degree=${degree}&type=${type}&gender=${gender}`);
        return response.data;
    } catch (error) {
        console.error('Registration error:', error);
        throw error;
    }
}