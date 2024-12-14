package com.nashel.ms_cart.exception;

import com.nashel.ms_cart.models.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.NoSuchElementException;
import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse("BAD REQUEST", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", "", HttpStatus.INTERNAL_SERVER_ERROR.value());
        log.error("exception occurred", ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoSuchElementException ex) {
        ErrorResponse errorResponse = new ErrorResponse("NOT FOUND", ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Collect validation error messages including the field name
        StringBuilder errorMessages = new StringBuilder();

        // Iterate over each validation error
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            fieldName = fieldName.replaceAll("\\[\\]", "");  // Remove "[]"

            // Get the error message
            String errorMessage = error.getDefaultMessage();

            // Append the cleaned-up field name and error message
            errorMessages.append("Field: ").append(fieldName)
                    .append(", Error: ").append(errorMessage)
                    .append("; ");
        });

        // Create ErrorResponse with detailed validation errors
        ErrorResponse errorResponse = new ErrorResponse(
                "VALIDATION FAILED",
                errorMessages.toString(),
                HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleInvalidUUID(MethodArgumentTypeMismatchException ex) {
        // Check if the exception involves a UUID type mismatch
        if (ex.getRequiredType().equals(UUID.class)) {
            // Create a custom error response for invalid UUID format
            ErrorResponse errorResponse = new ErrorResponse(
                    "INVALID UUID FORMAT",
                    "The provided UUID is invalid: " + ex.getValue(),
                    HttpStatus.BAD_REQUEST.value()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // Handle other cases of MethodArgumentTypeMismatchException (optional)
        ErrorResponse errorResponse = new ErrorResponse(
                "BAD REQUEST",
                "Invalid argument type: " + ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}