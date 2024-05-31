import React, {createContext, useState, useContext} from 'react';
import {loginUser} from "../api/StudyMateApiService";

export const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext)

export default function AuthProvider(props) {

    const [isAuthenticated, setAuthenticated] = useState(false);
    const [username, setUsername] = useState(null); // Add this line

    async function login(username, password) {
        try {
            const response =  await loginUser(username, password);
            if (response) {
                setAuthenticated(true);
                setUsername(username); // Add this line
                return true;
            } else {
                setAuthenticated(false);
                setUsername(null); // Add this line
                return false;
            }
        } catch (error) {
            console.error('Failed to login:', error);
            setAuthenticated(false);
            setUsername(null); // Add this line
            return false;
        }
    }

    function logout() {
        setAuthenticated(false);
        setUsername(null); // Add this line
    }

    return (
        <AuthContext.Provider value={{isAuthenticated, login, logout, username}}>
            {props.children}
        </AuthContext.Provider>
    );
}