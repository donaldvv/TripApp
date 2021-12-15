package com.donald.service.advice;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;



@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestMediaTypeNotValid {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ApiError handleException(HttpMessageNotReadableException ex) {

        Map<String, String> errors = new HashMap<>();

        if (ex.getCause() instanceof InvalidFormatException){
            errors.put("invalid format", "Invalid type/format for atleast 1 of the values!");
        }
        if (ex.getCause() instanceof JsonParseException){
            errors.put("json parsing error", "Json request content is non-well-formed (not corresponding to JSON syntax)!");
        }
        if (ex.getCause() instanceof JsonEOFException){
            errors.put("json EOF error", "Unexpected end-of-input: expected close marker for Object!");
        }
        if (ex.getCause() instanceof JsonMappingException){
            errors.put("mapping error", "Value mapping error!");
        }

        ApiError apiError = new ApiError(415, "Request media type not valid");
        apiError.setValidationErrors(errors);

        return apiError;
    }

}
