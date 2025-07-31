package com.ipass.taskManager;

import com.ipass.taskManager.dto.TaskRequestDto;
import com.ipass.taskManager.model.User;
import com.ipass.taskManager.repository.TaskRepository;
import com.ipass.taskManager.service.TaskService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;


@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private TaskRequestDto task;
    private User user;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        user = new User();
        user.setId(UUID.randomUUID());

        task = new TaskRequestDto();
        task.setTitulo("Test Task");
        task.setDescricao("Test Description");
        task.setUserId(user.getId());
    }

    @Test
    @DisplayName("Deve criar uma tarefa com sucesso")
    void createTaskSuccessfully() {}
}