package com.donald.service.exception;

public class TokenRefreshException extends RuntimeException{

    public TokenRefreshException(String message, String token) {
        super(String.format("Refresh Token: %s. %s", token, message));
    }
}
