package com.donald.service.validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = TimeSpanValidator.class)
public @interface TimeSpan {

    String message() default "{constraints.timespan}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    // getting the fields of the object that will be validated
    String startTime();
    String endTime();
}
