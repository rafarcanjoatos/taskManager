package com.ipass.taskManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ipass.taskManager.dto.TaskRequestDto;
import com.ipass.taskManager.model.Task;
import com.ipass.taskManager.model.TaskStatus;
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
    private Task task;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        user = new User();
        user.setId(UUID.randomUUID());
        user.setNome("Test User");
        user.setEmail("test@user.com");

        taskRequest = new TaskRequestDto();
        taskRequest.setTitulo("Test Task");
        taskRequest.setDescricao("Test Description");
        taskRequest.setUsuarioId(user.getId());

        task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitulo(taskRequest.getTitulo());
        task.setDescricao(taskRequest.getDescricao());
        task.setUsuario(user);
        task.setStatus(TaskStatus.PENDENTE);
    }

    @Test
    @DisplayName("Deve criar uma tarefa com sucesso")
    void createTask_whenUserExists_shouldReturnNewTask() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task savedTask = invocation.getArgument(0);
            savedTask.setId(UUID.randomUUID());
            return savedTask;
        });

        Task createdTask = taskService.createTask(taskRequest);

        assertNotNull(createdTask);
        assertEquals(taskRequest.getTitulo(), createdTask.getTitulo());
        assertEquals(TaskStatus.PENDENTE, createdTask.getStatus());
        assertEquals(user.getId(), createdTask.getUsuario().getId());
        verify(userRepository, times(1)).findById(user.getId());
        verify(taskRepository, times(1)).save(any(Task.class));
    }


    @Test
    @DisplayName("Deve encontrar uma tarefa pelo ID com sucesso")
    void getTaskById_whenTaskExists_shouldReturnTask() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        Task foundTask = taskService.getTaskById(task.getId());

        assertNotNull(foundTask);
        assertEquals(task.getId(), foundTask.getId());
        verify(taskRepository, times(1)).findById(task.getId());
    }

}