package com.example.student_task_tracker.exception;

/**
 * Thrown when a Task request body fails validation
 * (e.g. title is missing or blank).
 */
public class InvalidTaskException extends RuntimeException {

    public InvalidTaskException(String message) {
        super(message);
    }
}
