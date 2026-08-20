package com.danielamarjina.carinsurance.exception;


import java.util.UUID;

public class InsurancePolicyNotFoundException extends RuntimeException {
    public InsurancePolicyNotFoundException(UUID carId) {
        super(String.format("This car '%s' does not have an insurance policy",carId));
    }
}
