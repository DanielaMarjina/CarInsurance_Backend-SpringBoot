package com.danielamarjina.carinsurance.exception;

import com.danielamarjina.carinsurance.dto.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(OwnerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOwnerNotFound(OwnerNotFoundException exception){
        ErrorResponse errorResponse=ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Owner not found")
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception){
        String message=exception.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();

        ErrorResponse errorResponse=ErrorResponse.builder().
                timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation failed")
                .message(message)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}
