package com.donald.service.advice;

import com.donald.service.exception.TokenRefreshException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TokenAdvice {

    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleTokenRefreshEx(TokenRefreshException ex) {
        return new ApiError(HttpStatus.FORBIDDEN.value(), ex.getMessage());
    }
}
