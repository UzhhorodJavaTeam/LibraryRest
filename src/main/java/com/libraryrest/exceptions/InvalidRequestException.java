package com.libraryrest.exceptions;

import org.springframework.validation.Errors;

/**
 * Created by superuser on 29.05.15.
 */
@SuppressWarnings("serial")
public class InvalidRequestException extends RuntimeException {
    private Errors errors;

    public InvalidRequestException(String message, Errors errors) {
        super(message);
        this.errors = errors;
    }

    public Errors getErrors() { return errors; }
}