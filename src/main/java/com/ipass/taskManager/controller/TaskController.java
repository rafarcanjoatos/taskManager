package com.ipass.taskManager.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ipass.taskManager.dto.TaskRequestDto;
import com.ipass.taskManager.dto.TaskResponseDto;
import com.ipass.taskManager.model.Task;
import com.ipass.taskManager.model.TaskStatus;
import com.ipass.taskManager.service.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Tarefas", description = "Operações relacionadas a tarefas na aplicação.")
@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @Operation(
        summary = "Cria uma nova tarefa",
        description = "Cria uma nova tarefa para um usuário específico, com os detalhes fornecidos."
    )
    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskRequestDto taskRequestDto) {
        Task createdTask = taskService.createTask(taskRequestDto);
        return new ResponseEntity<>(TaskResponseDto.fromEntity(createdTask), HttpStatus.CREATED);
    }

    @Operation(
        summary = "Busca uma tarefa por ID",
        description = "Busca apenas uma tarefa por ID e retorna seus detalhes"
    )
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable UUID id) {
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(TaskResponseDto.fromEntity(task));
    }

    @Operation(
        summary = "Busca todas as tarefas ou filtra por status",
        description = "Busca todas as tarefas ou filtra por um status específico (PENDENTE, EM_ANDAMENTO, CONCLUIDA). O status EXCLUIDA não é permitido para consulta."
    )
    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAllTasks(
        @Parameter(description = "Status para filtrar as tarefas.", schema = @Schema(type = "string", allowableValues = {"PENDENTE", "EM_ANDAMENTO", "CONCLUIDA"}))
        @RequestParam(required = false) TaskStatus status
    ) {
        TaskStatus taskStatus = null;
        if (status != null) {
            if (status == TaskStatus.EXCLUIDA) {
                throw new IllegalArgumentException("O status 'EXCLUIDA' não é válido para consulta.");
            }
            try {
                taskStatus = status;
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Status inválido: " + status);
            }
        }
        List<Task> tasks = taskService.getAllTasks(taskStatus);
        List<TaskResponseDto> dtos = tasks.stream()
                .map(TaskResponseDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(
        summary = "Atualiza os detalhes da tarefa",
        description = "Atualiza os detalhes de uma tarefa por ID, como título e descrição"
    )
    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTaskById(@PathVariable UUID id, @Valid @RequestBody TaskRequestDto taskRequestDto) {
        Task updatedTask = taskService.updateTask(id, taskRequestDto);
        return ResponseEntity.ok(TaskResponseDto.fromEntity(updatedTask));
    }

    @Operation(
        summary = "Atualiza o status da tarefa",
        description = "Atualiza somente o status de uma tarefa por ID"
    )
    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponseDto> updateTaskStatusById(
        @PathVariable UUID id, 
        @Parameter(description = "Status para atualizar a tarefa.", schema = @Schema(type = "string", allowableValues = {"PENDENTE", "EM_ANDAMENTO", "CONCLUIDA"}))
        @RequestParam(required = true) TaskStatus status
    ) {
        Task updatedTask = taskService.updateTaskStatus(id, status);
        return ResponseEntity.ok(TaskResponseDto.fromEntity(updatedTask));
    }

    
    @Operation(
        summary = "Deleta uma tarefa",
        description = "Deleta uma tarefa por ID, se não houver subtarefas"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<TaskResponseDto> deleteTaskById(@PathVariable UUID id) {
        Task deletedTask = taskService.deleteTask(id);
        return ResponseEntity.ok(TaskResponseDto.fromEntity(deletedTask));
    }

}