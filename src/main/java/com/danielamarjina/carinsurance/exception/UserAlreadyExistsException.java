package com.danielamarjina.carinsurance.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String value) {
        super(String.format("User by '%s' already exists",value));
    }

}
