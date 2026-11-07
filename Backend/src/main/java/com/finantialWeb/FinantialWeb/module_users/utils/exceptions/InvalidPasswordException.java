package com.finantialWeb.FinantialWeb.module_users.utils.exceptions;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException(String message) {
        super(message);
    }
}
