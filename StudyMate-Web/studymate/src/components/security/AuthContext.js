import React, {createContext, useState, useContext} from 'react';
import {loginUser} from "../api/StudyMateApiService";

export const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext)



export default function AuthProvider(props) {

    const [isAuthenticated, setAuthenticated] = useState(false);

    async function login(username, password) {
        try {
            const response =  await loginUser(username, password);
            if (response) {
                setAuthenticated(true);
                return true;
            } else {
                setAuthenticated(false);
                return false;
            }
        } catch (error) {
            console.error('Failed to login:', error);
            setAuthenticated(false);
            return false;
        }
    }

    function logout() {
        setAuthenticated(false);
    }


    return (
        <AuthContext.Provider value={{isAuthenticated, login, logout}}>
            {props.children}
        </AuthContext.Provider>
    );
}