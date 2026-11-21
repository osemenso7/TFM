package com.finantialWeb.FinantialWeb.module_imports.utils.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidImportException extends RuntimeException {

    public InvalidImportException(String message) {
        super(message);
    }
}
