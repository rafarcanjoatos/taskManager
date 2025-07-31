package com.ipass.taskManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipass.taskManager.dto.TaskRequestDto;
import com.ipass.taskManager.model.User;
import com.ipass.taskManager.repository.TaskRepository;
import com.ipass.taskManager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setNome("Test User");
        testUser.setEmail("test@example.com");
        testUser = userRepository.save(testUser);
    }

    @Test
    void createTask_shouldReturnCreatedTask() throws Exception {
        TaskRequestDto taskRequest = new TaskRequestDto();
        taskRequest.setTitulo("Teste de Integração");
        taskRequest.setDescricao("Testando a integração da aplicação");
        taskRequest.setUserId(testUser.getId());

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.titulo").value("Integration Test Task"))
                .andExpect(jsonPath("$.status").value("PENDENTE"))
                .andExpect(jsonPath("$.userId").value(testUser.getId().toString()));
    }
}