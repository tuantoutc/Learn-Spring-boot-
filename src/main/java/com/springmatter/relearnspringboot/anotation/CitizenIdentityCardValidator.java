package com.springmatter.relearnspringboot.anotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CitizenIdentityCardValidator implements ConstraintValidator<CitizenIdentityCard, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s == null || s.isBlank()) return true;
        return s.matches("\\d{12}$");
    }
}
