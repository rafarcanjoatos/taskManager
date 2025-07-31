package com.ipass.taskManager.service;

import com.ipass.taskManager.dto.TaskRequestDto;
import com.ipass.taskManager.model.Task;
import com.ipass.taskManager.model.TaskStatus;
import com.ipass.taskManager.model.User;
import com.ipass.taskManager.repository.TaskRepository;
import com.ipass.taskManager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional
    public Task createTask(TaskRequestDto taskRequestDto) {
        User user = userRepository.findById(taskRequestDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + taskRequestDto.getUserId()));

        Task task = new Task();
        task.setTitulo(taskRequestDto.getTitulo());
        task.setDescricao(taskRequestDto.getDescricao());
        task.setUser(user);

        return taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public Optional<Task> getTaskById(UUID id) {
        return taskRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Task> getAllTasks(TaskStatus status) {
        if (status != null) {
            return taskRepository.findByStatus(status);
        }
        return taskRepository.findAll();
    }

    @Transactional
    public Task updateTask(UUID id, TaskRequestDto taskRequestDto) {
        Task existingTask = getTaskById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada com o id: " + id));

        existingTask.setTitulo(taskRequestDto.getTitulo());
        existingTask.setDescricao(taskRequestDto.getDescricao());

        return taskRepository.save(existingTask);
    }

    @Transactional
    public Task updateTaskStatus(UUID id, TaskStatus status) {
        Task existingTask = getTaskById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada com o id: " + id));

        existingTask.setStatus(status);

        return taskRepository.save(existingTask);
    }
}
