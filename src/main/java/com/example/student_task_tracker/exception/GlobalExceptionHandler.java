package com.example.student_task_tracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Central exception handler for the Student Task Tracker API.
 *
 * Catches exceptions thrown by the service layer and converts them into
 * consistent, structured JSON error responses rather than raw stack traces.
 *
 * Example error response body:
 * {
 *   "timestamp": "2026-05-23T10:15:30",
 *   "status": 400,
 *   "error": "Bad Request",
 *   "message": "Task title must not be blank."
 * }
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation failures — returns 400 Bad Request.
     */
    @ExceptionHandler(InvalidTaskException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidTask(InvalidTaskException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Handles missing task lookups — returns 404 Not Found.
     */
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTaskNotFound(TaskNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // -------------------------------------------------------------------------
    // Private helper
    // -------------------------------------------------------------------------

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}
