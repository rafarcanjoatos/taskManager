package com.ipass.taskManager.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.ipass.taskManager.model.Task;
import com.ipass.taskManager.model.TaskStatus;

import lombok.Data;

@Data
public class TaskResponseDto {
    private UUID id;
    private String titulo;
    private String descricao;
    private TaskStatus status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataConclusao;
    private UUID usuarioId;

    public static TaskResponseDto fromEntity(Task task) {
        if (task == null) {
            return null;
        }
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(task.getId());
        dto.setTitulo(task.getTitulo());
        dto.setDescricao(task.getDescricao());
        dto.setStatus(task.getStatus());
        dto.setDataCriacao(task.getDataCriacao());
        dto.setDataConclusao(task.getDataConclusao());
        dto.setUsuarioId(task.getUsuario().getId());
        
        return dto;
    }
}