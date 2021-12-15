package com.donald.service.advice;

import com.donald.service.exception.DateFormatException;
import com.donald.service.exception.TimeSpanException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EntityValidityAdvice {

    @ExceptionHandler(DateFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleDateFormatException(DateFormatException ex) {
        return new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(TimeSpanException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleTimeSpanException(TimeSpanException ex) {
        return new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

}
