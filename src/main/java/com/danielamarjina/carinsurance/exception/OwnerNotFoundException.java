package com.danielamarjina.carinsurance.exception;

public class OwnerNotFoundException extends RuntimeException{

    public OwnerNotFoundException(String email){
        super("Owner with email '"+email+"' was not found.");
    }
}
