import React, {createContext, useState, useContext} from 'react';
import {loginUser} from "../api/StudyMateApiService";

export const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext)

export default function AuthProvider(props) {

    const [isAuthenticated, setAuthenticated] = useState(false);
    const [username, setUsername] = useState(null);
    const [token, setToken] = useState(null);

    async function login(username, password) {
        try {
            const response = await loginUser(username, password);
            if (response) {
                setAuthenticated(true);
                setUsername(username);
                setToken(response);
                localStorage.setItem('username', username); // Store the username in localStorage
                return true;
            } else {
                setAuthenticated(false);
                setUsername(null);
                setToken(null); // Clear the token
                localStorage.removeItem('username'); // Remove the username from localStorage
                return false;
            }
        } catch (error) {
            console.error('Failed to login:', error);
            setAuthenticated(false);
            setUsername(null);
            setToken(null); // Clear the token
            localStorage.removeItem('username'); // Remove the username from localStorage
            return false;
        }
    }

    function logout() {
        setAuthenticated(false);
        setUsername(null);
        setToken(null); // Clear the token
    }

    return (
        <AuthContext.Provider value={{isAuthenticated, login, logout, username, token}}>
            {props.children}
        </AuthContext.Provider>
    );
}