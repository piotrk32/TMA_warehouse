package com.example.tma_warehouse.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = PageSizeValidator.class)
public @interface PageSize {

    String message() default "The size of page must be a number between 1 and 100";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}