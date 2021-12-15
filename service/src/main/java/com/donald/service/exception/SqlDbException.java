package com.donald.service.exception;

public class SqlDbException extends RuntimeException {
    public SqlDbException(String message) {
        super(message);
    }
}
