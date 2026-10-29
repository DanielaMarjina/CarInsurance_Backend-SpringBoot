package com.danielamarjina.carinsurance.exception;

import java.util.UUID;

public class OverlappingInsurancePolicyException extends RuntimeException {
    public OverlappingInsurancePolicyException(UUID carId) {
        super(String.format("Car with id '%s' already has an insurance policy overlapping this period",carId));
    }
}
