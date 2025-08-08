package com.ipass.taskManager.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ipass.taskManager.dto.SubtaskRequestDto;
import com.ipass.taskManager.dto.SubtaskResponseDto;
import com.ipass.taskManager.model.Subtask;
import com.ipass.taskManager.model.TaskStatus;
import com.ipass.taskManager.service.SubtaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Subtarefas", description = "Operações relacionadas a subtarefas na aplicação.")
@RestController
@RequestMapping("/tarefas/{tarefaId}/subtarefas")
@RequiredArgsConstructor
public class SubtaskController {
    
    private final SubtaskService subtaskService;
    
    
    @Operation(
        summary = "Cria uma subtarefa",
        description = "Cria uma subtarefa para uma tarefa específica, com os detalhes fornecidos"
    )
    @PostMapping
    public ResponseEntity<SubtaskResponseDto> createSubtask(@PathVariable UUID tarefaId, @Valid @RequestBody SubtaskRequestDto subtaskRequestDto) {
        Subtask createdSubtask = subtaskService.createSubtask(tarefaId, subtaskRequestDto);
        return new ResponseEntity<>(SubtaskResponseDto.fromEntity(createdSubtask), HttpStatus.CREATED);
    }


    @Operation(
        summary = "Lista subtarefas de uma tarefa",
        description = "Lista todas as subtarefas de uma tarefa específica e retorna seus detalhes"
    )
    @GetMapping
    public ResponseEntity<List<SubtaskResponseDto>> getSubtasksByParentTaskId(@PathVariable UUID tarefaId) {
        List<Subtask> subtasks = subtaskService.getSubtasksByParentTaskId(tarefaId);
        List<SubtaskResponseDto> responseDtos = subtasks.stream()
                .map(SubtaskResponseDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }


    @Operation(
        summary = "Busca uma subtarefa por ID",
        description = "Busca uma subtarefa por ID e retorna seus detalhes"
    )
    @GetMapping("/{subtarefaId}")
    public ResponseEntity<SubtaskResponseDto> getSubtaskById(@PathVariable UUID subtarefaId) {
        Subtask subtask = subtaskService.getSubtaskById(subtarefaId);
        return ResponseEntity.ok(SubtaskResponseDto.fromEntity(subtask));
    }


    @Operation(
        summary = "Atualiza uma subtarefa por ID",
        description = "Atualiza os detalhes de uma subtarefa por ID, como título e descrição"
    )
    @PatchMapping("/{subtarefaId}")
    public ResponseEntity<SubtaskResponseDto> updateSubtaskById(@PathVariable UUID subtarefaId, @Valid @RequestBody SubtaskRequestDto subtaskRequestDto) {
        Subtask updatedSubtask = subtaskService.updateSubtaskById(subtarefaId, subtaskRequestDto);
        return ResponseEntity.ok(SubtaskResponseDto.fromEntity(updatedSubtask));
    }


    @Operation(
        summary = "Atualiza o status da subtarefa",
        description = "Atualiza o status de uma subtarefa por ID"
    )
    @PatchMapping("/{subtarefaId}/status")
    public ResponseEntity<SubtaskResponseDto> updateSubtaskStatusById(@PathVariable UUID subtarefaId, @RequestParam TaskStatus status) {
        Subtask updatedSubtask = subtaskService.updateSubtaskStatus(subtarefaId, status);
        return ResponseEntity.ok(SubtaskResponseDto.fromEntity(updatedSubtask));
    }
   
}