package com.ipass.taskManager.controller;

import com.ipass.taskManager.dto.TaskRequestDto;
import com.ipass.taskManager.dto.TaskResponseDto;
import com.ipass.taskManager.model.Task;
import com.ipass.taskManager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskRequestDto taskRequestDto) {
        Task createdTask = taskService.createTask(taskRequestDto);
        return new ResponseEntity<>(TaskResponseDto.fromEntity(createdTask), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable UUID id) {
        return taskService.getTaskById(id)
                .map(TaskResponseDto::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}