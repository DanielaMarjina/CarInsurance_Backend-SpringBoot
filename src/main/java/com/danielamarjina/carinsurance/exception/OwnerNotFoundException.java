package com.danielamarjina.carinsurance.exception;

import java.util.UUID;

public class OwnerNotFoundException extends RuntimeException{

    public OwnerNotFoundException(String email){
        super(String.format("Owner with email '%s' was not found.",email));
    }

    public OwnerNotFoundException(UUID id){
        super(String.format("Owner with id '%s' was not found.",id));
    }
}
