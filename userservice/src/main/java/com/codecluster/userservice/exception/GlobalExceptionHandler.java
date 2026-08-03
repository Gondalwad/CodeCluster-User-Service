package com.codecluster.userservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

/*
 * Global exception handler for the application.
 *
 * @RestControllerAdvice intercepts exceptions thrown
 * by any REST controller and converts them into
 * standardized HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
     * Handles ResourceAlreadyExistsException.
     *
     * Returns HTTP 409 (Conflict) along with a
     * standardized error response.
     */
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExists(
            ResourceAlreadyExistsException ex,
            HttpServletRequest request) {

        /*
         * Create an ErrorResponse object containing
         * details about the exception.
         */
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                OffsetDateTime.now()
        );

        // Return HTTP 409 response.
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /*
     * Handles ResourceNotFoundException.
     *
     * Returns HTTP 404 (Not Found) when
     * the requested resource does not exist.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                OffsetDateTime.now()
        );

        // Return HTTP 404 response.
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /*
     * Handles validation failures caused by
     * @Valid annotations on request DTOs.
     *
     * Returns HTTP 400 (Bad Request).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        /*
         * Retrieve the first validation error
         * from the request.
         */
        FieldError fieldError = ex.getBindingResult().getFieldError();

        /*
         * Use the validation message if available,
         * otherwise return a default message.
         */
        String message = (fieldError != null && fieldError.getDefaultMessage() != null)
                ? fieldError.getDefaultMessage()
                : "Validation failed";

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                request.getRequestURI(),
                OffsetDateTime.now()
        );

        // Return HTTP 400 response.
        return ResponseEntity.badRequest().body(error);
    }

    /*
     * Handles all unhandled exceptions.
     *
     * Acts as a fallback so that unexpected
     * errors are returned in a consistent format
     * instead of exposing stack traces.
     *
     * Returns HTTP 500 (Internal Server Error).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                OffsetDateTime.now()
        );

        // Return HTTP 500 response.
        return ResponseEntity.internalServerError().body(error);
    }
}