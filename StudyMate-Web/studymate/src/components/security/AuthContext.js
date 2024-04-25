import React, {createContext, useState, useContext} from 'react';

export const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext)



export default function AuthProvider(props) {

    const [isAuthenticated, setAuthenticated] = useState(false);

    function login(username, password) {
        if(username === 'shahar' && password === 'admin') {
            setAuthenticated(true)
            return true
        } else {
            setAuthenticated(false)
            return false
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