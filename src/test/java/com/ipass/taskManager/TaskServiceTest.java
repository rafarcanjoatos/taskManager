package com.ipass.taskManager;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ipass.taskManager.dto.TaskRequestDto;
import com.ipass.taskManager.model.User;


@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

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