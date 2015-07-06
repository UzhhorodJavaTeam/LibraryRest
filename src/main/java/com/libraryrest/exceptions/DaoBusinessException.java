package com.libraryrest.exceptions;

public class DaoBusinessException extends DaoException {

    public DaoBusinessException(String message) {
        super(message);
    }

    public DaoBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
