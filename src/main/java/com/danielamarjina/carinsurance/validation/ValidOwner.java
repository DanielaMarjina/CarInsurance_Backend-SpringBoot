package com.danielamarjina.carinsurance.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OwnerValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOwner {
    String message() default "Invalid owner data";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
