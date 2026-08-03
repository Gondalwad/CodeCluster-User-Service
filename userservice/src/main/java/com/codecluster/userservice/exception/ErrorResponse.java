package com.codecluster.userservice.exception;

import java.time.OffsetDateTime;

/*
 * Represents the standard structure of an error response
 * returned to the client when an exception occurs.
 *
 * This class is used by the GlobalExceptionHandler to
 * provide consistent error information for all API errors.
 */
public class ErrorResponse {

    /*
     * HTTP status code
     * (e.g. 400, 404, 409, 500).
     */
    private int status;

    /*
     * Short description of the HTTP error
     * (e.g. Bad Request, Not Found).
     */
    private String error;

    /*
     * Detailed message describing
     * what went wrong.
     */
    private String message;

    /*
     * API endpoint that generated
     * the error.
     */
    private String path;

    /*
     * Date and time when
     * the error occurred.
     */
    private OffsetDateTime timestamp;

    /*
     * Default constructor required by
     * serialization frameworks.
     */
    public ErrorResponse() {
    }

    /*
     * Parameterized constructor used to create
     * a complete error response.
     */
    public ErrorResponse(int status,
                         String error,
                         String message,
                         String path,
                         OffsetDateTime timestamp) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timestamp = timestamp;
    }

    // Standard getters and setters.

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }
}