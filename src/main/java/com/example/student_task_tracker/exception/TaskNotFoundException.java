package com.example.student_task_tracker.exception;

/**
 * Thrown when a requested Task ID does not exist in the store.
 */
public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id) {
        super("Task not found with id: " + id);
    }
}
