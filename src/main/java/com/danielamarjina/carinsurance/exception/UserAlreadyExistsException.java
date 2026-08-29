package com.danielamarjina.carinsurance.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String messge) {
        super(messge);
    }

}
