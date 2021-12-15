package com.donald.service.validator;
import com.donald.service.exception.DateFormatException;
import com.donald.service.exception.TimeSpanException;
import lombok.SneakyThrows;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDateTime;


public class TimeSpanValidator implements ConstraintValidator<TimeSpan, Object> {

    private String startTimeFieldName;
    private String endTimeFieldName;

    @Override
    public void initialize(TimeSpan constraintAnnotation) {
        startTimeFieldName = constraintAnnotation.startTime();
        endTimeFieldName = constraintAnnotation.endTime();
    }

    @SneakyThrows({NoSuchFieldException.class, IllegalAccessException.class})
    @Override
    public boolean isValid(Object objectToBeValidated, ConstraintValidatorContext context) {
        try {
            Field startTimeField = objectToBeValidated.getClass().getDeclaredField(startTimeFieldName);
            Field endTimeField = objectToBeValidated.getClass().getDeclaredField(endTimeFieldName);

            startTimeField.setAccessible(true);
            endTimeField.setAccessible(true);

            if(!(startTimeField.get(objectToBeValidated) instanceof LocalDateTime)) {
                throw new DateFormatException(String.format("Fields should be of type LocalDateTime, Field: %s", startTimeFieldName));
            }
            if(!(endTimeField.get(objectToBeValidated) instanceof LocalDateTime)) {
                throw new DateFormatException(String.format("Fields should be of type LocalDateTime. Field: %s", startTimeFieldName));
            }
            LocalDateTime startTime = (LocalDateTime) startTimeField.get(objectToBeValidated);
            LocalDateTime endTime = (LocalDateTime) endTimeField.get(objectToBeValidated);


            if(startTime.isBefore(LocalDateTime.now()))
                throw new TimeSpanException(String.format("The date & time of the field: %s, must be a future date and time", startTimeFieldName));
            if(endTime.isBefore(LocalDateTime.now()))
                throw new TimeSpanException(String.format("The date & time of the field: %s, must be a future date and time", endTimeFieldName));
            if (!startTime.isBefore(endTime))
                throw new TimeSpanException(String.format("The date & time of the field: %s, must be after the " +
                        "date & time of the field: %s", endTimeFieldName, startTimeFieldName));

            return true;
        } catch (NoSuchFieldException e) {
            throw new NoSuchFieldException("Field mismatch!");
        } catch (IllegalAccessException e){
            throw new IllegalAccessException("Problem while trying to access objects fields!");
        }

    }
}
