package com.example.tma_warehouse.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = PageDirectionValidator.class)
public @interface PageDirection {

    String message() default "Direction must be either 'ASC' or 'DESC'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
