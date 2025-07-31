package com.ipass.taskManager.service;

import com.ipass.taskManager.dto.TaskRequestDto;
import com.ipass.taskManager.model.Task;
import com.ipass.taskManager.model.User;
import com.ipass.taskManager.repository.TaskRepository;
import com.ipass.taskManager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
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

    public Optional<Task> getTaskById(UUID id) {
        return taskRepository.findById(id);
    }

}