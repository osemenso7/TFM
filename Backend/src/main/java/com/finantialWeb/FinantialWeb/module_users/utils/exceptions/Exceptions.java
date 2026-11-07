package com.finantialWeb.FinantialWeb.module_users.utils.exceptions;

public class Exceptions {

    private Exceptions() {
        // Evitar instanciación de la clase
    }

    //Constants for db entity fields
    public static final String USER_NOT_FOUND = "User not found with id: ";
    public static final String USER_INVALID_CREDENTIALS = "Invalid credentials";
    public static final String USER_ALREADY_EXISTS = "Username or email already exists";
    public static final String USER_NOT_AUTHENTICATED = "User not authenticated";
}
