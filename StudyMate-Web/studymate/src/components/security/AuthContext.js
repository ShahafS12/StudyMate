import React, { createContext, useState, useEffect, useContext } from 'react';
import { loginUser, validateToken } from '../api/StudyMateApiService'; // Ensure correct path

export const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export default function AuthProvider(props) {
    const [isAuthenticated, setAuthenticated] = useState(false);
    const [username, setUsername] = useState(null);
    const [token, setToken] = useState(null);

    useEffect(() => {
        const storedUsername = localStorage.getItem('username');
        const storedToken = localStorage.getItem('authToken'); // Ensure you store the token as well

        if (storedUsername && storedToken) {
            validateToken(storedToken).then(isValid => {
                if (isValid) {
                    setAuthenticated(true);
                    setUsername(storedUsername);
                    setToken(storedToken);
                } else {
                    localStorage.removeItem('username');
                    localStorage.removeItem('authToken');
                }
            }).catch(() => {
                localStorage.removeItem('username');
                localStorage.removeItem('authToken');
            });
        }
    }, []);

    async function login(username, password) {
        try {
            const response = await loginUser(username, password);
            if (response) {
                setAuthenticated(true);
                setUsername(username);
                setToken(response);
                localStorage.setItem('username', username); // Store username
                localStorage.setItem('authToken', response); // Store token
                return true;
            } else {
                setAuthenticated(false);
                setUsername(null);
                setToken(null); // Clear the token
                localStorage.removeItem('username'); // Remove the username
                localStorage.removeItem('authToken'); // Remove the token
                return false;
            }
        } catch (error) {
            console.error('Failed to login:', error);
            setAuthenticated(false);
            setUsername(null);
            setToken(null); // Clear the token
            localStorage.removeItem('username'); // Remove the username
            localStorage.removeItem('authToken'); // Remove the token
            return false;
        }
    }

    function logout() {
        setAuthenticated(false);
        setUsername(null);
        setToken(null); // Clear the token
        localStorage.removeItem('username'); // Remove the username
        localStorage.removeItem('authToken'); // Remove the token
    }

    return (
        <AuthContext.Provider value={{ isAuthenticated, login, logout, username, token }}>
            {props.children}
        </AuthContext.Provider>
    );
}
