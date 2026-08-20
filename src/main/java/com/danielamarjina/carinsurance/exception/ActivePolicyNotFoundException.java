package com.danielamarjina.carinsurance.exception;

import java.util.UUID;

public class ActivePolicyNotFoundException extends RuntimeException {
    public ActivePolicyNotFoundException(UUID carId) {
        super(String.format("No active policy for this car: '%s'.",carId));
    }
}
