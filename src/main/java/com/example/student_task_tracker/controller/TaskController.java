package com.example.student_task_tracker.controller;

import com.example.student_task_tracker.exception.TaskNotFoundException;
import com.example.student_task_tracker.model.Task;
import com.example.student_task_tracker.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for the /tasks resource.
 *
 * Endpoints:
 *   GET    /tasks        – retrieve all tasks
 *   GET    /tasks/{id}   – retrieve a single task by ID
 *   POST   /tasks        – create a new task
 *   PUT    /tasks/{id}   – update an existing task
 *   DELETE /tasks/{id}   – delete a task
 *
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // -------------------------------------------------------------------------
    // GET endpoints
    // -------------------------------------------------------------------------

    // GET /tasks. Returns all tasks.
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    // GET /tasks/{id}. Returns the task with the given ID, or 404 Not Found if it doesn't exist.
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return ResponseEntity.ok(task);
    }

    // -------------------------------------------------------------------------
    // POST, PUT, DELETE endpoints
    // -------------------------------------------------------------------------

    /**
     * POST /tasks
     * Creates a new task. Returns 201 Created with the saved task.
     * Returns 400 Bad Request if the title is missing.
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task created = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /tasks/{id}
     * Updates an existing task. Returns 200 OK with the updated task.
     * Returns 404 Not Found if the ID doesn't exist.
     * Returns 400 Bad Request if the title is missing.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        Task updated = taskService.updateTask(id, task);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /tasks/{id}
     * Deletes the task with the given ID. Returns 204 No Content on success.
     * Returns 404 Not Found if the ID does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
