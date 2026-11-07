import React, { createContext, useState } from 'react';
import { requestLogin, requestRegister } from '../Services/AuthService';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [token, setToken] = useState(null);

    const register = async (credentials) => {
        await requestRegister(credentials);
    };

    const login = async (credentials) => {
        const data = await requestLogin(credentials);

        setUser(data.user);
        setToken(data.token);
    };

    const logout = () => {
        setUser(null);
        setToken(null);
    };

    const authContextValue = {
        user,
        token,
        isAuthenticated: !!token,
        login,
        logout,
        register
    };

    return (
        <AuthContext.Provider value={authContextValue}>
            {children}
        </AuthContext.Provider>
    );
};