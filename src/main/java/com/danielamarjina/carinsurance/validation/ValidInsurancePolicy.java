package com.danielamarjina.carinsurance.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = InsurancePolicyValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidInsurancePolicy {
    String message() default "Invalid insurance policy data";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
