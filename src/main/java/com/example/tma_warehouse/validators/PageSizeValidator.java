package com.example.tma_warehouse.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PageSizeValidator implements ConstraintValidator<PageSize, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isBlank()) {
            return false;
        }
        try {
            int number = Integer.parseInt(value);
            return (number > 0 && number <= 100);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
