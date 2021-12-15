package com.donald.service.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

@Target({METHOD, FIELD, ANNOTATION_TYPE, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EnumValidatorImpl.class)
public @interface EnumValidator {

    String message() default "Must be one of the valid enum values";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class < ? extends java.lang.Enum < ? >> enumClass();

    boolean ignoreCase() default false;

}
