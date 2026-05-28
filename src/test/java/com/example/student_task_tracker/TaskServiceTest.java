package com.example.student_task_tracker;

import com.example.student_task_tracker.model.Task;
import com.example.student_task_tracker.repository.TaskRepository;
import com.example.student_task_tracker.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TaskService.
 *
 * The TaskRepository is mocked so these tests run without any web server
 * or in-memory store — they only test the business logic in the service.
 *
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task sampleTask;

    @BeforeEach
    void setUp() {
        sampleTask = new Task(1L, "Study for exam", "Review chapters 1-5", "2026-06-10", false);
    }

    // -------------------------------------------------------------------------
    // getAllTasks
    // -------------------------------------------------------------------------

    // Normal path: repository has tasks → service returns them all. 
    @Test
    void getAllTasks_returnsAllTasks() {
        Task second = new Task(2L, "Submit assignment", "Upload to iLearn", "2026-06-15", false);
        when(taskRepository.findAll()).thenReturn(List.of(sampleTask, second));

        List<Task> result = taskService.getAllTasks();

        assertThat(result).hasSize(2);
        assertThat(result).contains(sampleTask, second);
    }


    // -------------------------------------------------------------------------
    // getTaskById
    // -------------------------------------------------------------------------

    // Normal path: ID exists, service returns the matching task. 
    @Test
    void getTaskById_returnsTask_whenIdExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));

        Optional<Task> result = taskService.getTaskById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Study for exam");
    }


    // -------------------------------------------------------------------------
    // CreateTask
    // -------------------------------------------------------------------------

    // Normal path: valid task, service saves it and returns the saved version.
    @Test
    void createTask_savesAndReturnsTask_whenTitleIsValid() {
        Task input = new Task(null, "Read textbook", "Chapter 3", "2026-06-20", false);
        Task saved = new Task(3L, "Read textbook", "Chapter 3", "2026-06-20", false);
        when(taskRepository.save(any(Task.class))).thenReturn(saved);

        Task result = taskService.createTask(input);

        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getTitle()).isEqualTo("Read textbook");
        verify(taskRepository, times(1)).save(any(Task.class));
    }


    // -------------------------------------------------------------------------
    // updateTask
    // -------------------------------------------------------------------------

    // Normal path: ID exists, valid data, service updates and returns the task.
    @Test
    void updateTask_updatesAndReturnsTask_whenIdExists() {
        Task updated = new Task(null, "Updated title", "New desc", "2026-07-01", true);
        Task saved = new Task(1L, "Updated title", "New desc", "2026-07-01", true);
        when(taskRepository.existsById(1L)).thenReturn(true);
        when(taskRepository.save(any(Task.class))).thenReturn(saved);

        Task result = taskService.updateTask(1L, updated);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Updated title");
        assertThat(result.isCompleted()).isTrue();
    }



    // -------------------------------------------------------------------------
    // deleteTask
    // -------------------------------------------------------------------------

    // Normal path: ID exists, repository delete is called once.
    @Test
    void deleteTask_deletesSuccessfully_whenIdExists() {
        when(taskReposito
    }

}
