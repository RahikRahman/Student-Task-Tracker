package com.example.student_task_tracker.service;

import com.example.student_task_tracker.exception.InvalidTaskException;
import com.example.student_task_tracker.exception.TaskNotFoundException;
import com.example.student_task_tracker.model.Task;
import com.example.student_task_tracker.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for task management.
 * Contains the business logic and input validation that sits between
 * the controller and the repository.
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Retrieve every task in the store.
     *
     * @return list of all tasks (may be empty, never null)
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Retrieve a single task by its ID.
     *
     * @param id the task ID
     * @return an Optional containing the task if found, or empty if not
     */
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    /**
     * Create a new task.
     * Validates that the title is present and non-blank.
     *
     * @param task the task to persist (ID will be assigned automatically)
     * @return the saved task with its assigned ID
     * @throws InvalidTaskException if the title is missing or blank
     */
    public Task createTask(Task task) {
        validateTask(task);
        task.setId(null); // ensure the repository assigns the ID
        return taskRepository.save(task);
    }

    /**
     * Update an existing task.
     * Validates that the task exists and that the new title is present and non-blank.
     *
     * @param id          the ID of the task to update
     * @param updatedTask the new task data
     * @return the updated task
     * @throws TaskNotFoundException if no task with the given ID exists
     * @throws InvalidTaskException  if the new title is missing or blank
     */
    public Task updateTask(Long id, Task updatedTask) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        validateTask(updatedTask);
        updatedTask.setId(id);
        return taskRepository.save(updatedTask);
    }

    /**
     * Delete a task by ID.
     *
     * @param id the task ID
     * @throws TaskNotFoundException if no task with the given ID exists
     */
    public void deleteTask(Long id) {
        if (!taskRepository.deleteById(id)) {
            throw new TaskNotFoundException(id);
        }
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    // Validates that a task has a non-blank title.
    private void validateTask(Task task) {
        if (task == null) {
            throw new InvalidTaskException("Request body must not be empty.");
        }
        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new InvalidTaskException("Task title must not be blank.");
        }
    }
}
