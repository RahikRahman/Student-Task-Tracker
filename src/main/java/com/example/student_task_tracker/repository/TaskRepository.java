package com.example.student_task_tracker.repository;

import com.example.student_task_tracker.model.Task;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory store for Task objects.
 * Uses a HashMap keyed by task ID, with an auto-incrementing ID counter.
 * Seeded with sample data so GET /tasks returns something useful immediately.
 */
@Repository
public class TaskRepository {

    private final Map<Long, Task> store = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public TaskRepository() {
        // Seed with example tasks
        save(new Task(null, "Submit COMP4060 Assignment 2", "Complete the CPD reflection report", "2026-06-15", false));
        save(new Task(null, "Study for COMP3000 exam", "Revise lecture slides 1-8", "2026-06-10", false));
    }

    // Persist a task. If the task has no ID, one is assigned automatically.
    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(idCounter.getAndIncrement());
        }
        store.put(task.getId(), task);
        return task;
    }

    // Return all tasks as an unordered list.
    public List<Task> findAll() {
        return new ArrayList<>(store.values());
    }

    // Return the task with the given ID, or empty if not found.
    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    // Delete a task by ID. Returns true if it existed, false otherwise.
    public boolean deleteById(Long id) {
        return store.remove(id) != null;
    }

    // Check whether a task with the given ID exists.
    public boolean existsById(Long id) {
        return store.containsKey(id);
    }
}
