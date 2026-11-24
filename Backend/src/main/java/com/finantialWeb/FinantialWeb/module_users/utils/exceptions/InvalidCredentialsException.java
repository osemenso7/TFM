package com.finantialWeb.FinantialWeb.module_users.utils.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidCredentialsException extends RuntimeException {

    private final HttpStatus status;

    public InvalidCredentialsException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
