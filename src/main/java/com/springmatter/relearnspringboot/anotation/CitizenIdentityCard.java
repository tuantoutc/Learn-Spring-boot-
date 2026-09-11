package com.springmatter.relearnspringboot.anotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) // chi duoc gan khi thuoc tinh la bien
@Retention(RetentionPolicy.RUNTIME) // hoat dong va ton tai khi code dang chay
@Constraint(validatedBy = CitizenIdentityCardValidator.class)// day la loi: chi dinh class nay chiu trach nhiem va kiem tra logic
public @interface CitizenIdentityCard {
    String message() default "The citizen identity card number consists of 12 digits.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
