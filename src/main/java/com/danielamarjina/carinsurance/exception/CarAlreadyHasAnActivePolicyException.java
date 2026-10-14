package com.danielamarjina.carinsurance.exception;

import java.util.UUID;

public class CarAlreadyHasAnActivePolicyException extends RuntimeException {
    public CarAlreadyHasAnActivePolicyException(UUID carId) {
        super(String.format("Car with id '%s' already has an active policy!",carId));
    }
}
