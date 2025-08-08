package com.ipass.taskManager;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ipass.taskManager.dto.TaskRequestDto;
import com.ipass.taskManager.model.Task;
import com.ipass.taskManager.model.User;
import com.ipass.taskManager.repository.TaskRepository;
import com.ipass.taskManager.repository.UserRepository;
import com.ipass.taskManager.service.TaskService;


@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private TaskRequestDto taskRequest;
    private User user;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        user = new User();
        user.setId(UUID.randomUUID());

        taskRequest = new TaskRequestDto();
        taskRequest.setTitulo("Test Task");
        taskRequest.setDescricao("Test Description");
        taskRequest.setUsuarioId(user.getId());
    }

}