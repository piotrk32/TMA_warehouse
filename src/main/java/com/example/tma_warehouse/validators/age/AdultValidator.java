package com.example.tma_warehouse.validators.age;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class AdultValidator implements ConstraintValidator<Adult, LocalDate> {

    private static final int ADULT_AGE = 18;

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext constraintValidatorContext) {
        return Period.between(birthDate, LocalDate.now()).getYears() >= ADULT_AGE;
    }
}
