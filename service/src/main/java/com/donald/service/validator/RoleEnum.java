package com.donald.service.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;


@Target({METHOD, FIELD, ANNOTATION_TYPE, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = RoleEnumValidator.class)
public @interface RoleEnum {

    String message() default "Roles, must be any of the specified values!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class < ? extends java.lang.Enum < ? >> enumClass();

    boolean ignoreCase() default false;

}
