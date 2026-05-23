package com.example.student_task_tracker;

import com.example.student_task_tracker.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the /tasks REST endpoints.
 *
 * @SpringBootTest loads the full application context (controller + service +
 * repository), so nothing is mocked. MockMvc is wired up from the
 * WebApplicationContext and fires real HTTP requests through the servlet layer.
 *
 * Test count: 3
 */
@SpringBootTest
class TaskControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    /**
     * Integration test 1 — GET /tasks
     *
     * Verifies that the endpoint returns 200 OK with a JSON array.
     * The repository is seeded with 2 tasks on startup, so the array is not empty.
     */
    @Test
    void getTasks_returns200WithTaskArray() throws Exception {
        mockMvc.perform(get("/tasks")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThan(0)));
    }

    /**
     * Integration test 2 — POST /tasks with a valid body
     *
     * Verifies that sending a well-formed task returns 201 Created.
     * The response body is JSON, and the returned task contains the
     * title and description that were sent.
     */
    @Test
    void createTask_returns201WithCreatedTask_whenBodyIsValid() throws Exception {
        String requestBody = """
                {
                  "title": "Integration test task",
                  "description": "Created during integration testing",
                  "dueDate": "2026-07-01",
                  "completed": false
                }
                """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Integration test task"))
                .andExpect(jsonPath("$.description").value("Created during integration testing"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    /**
     * Integration test 3 — GET /tasks/{id} with a non-existent ID
     *
     * Verifies that requesting a task that does not exist returns
     * 404 Not Found with a JSON error body containing a message field.
     */
    @Test
    void getTaskById_returns404_whenIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/tasks/9999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists());
    }
}
