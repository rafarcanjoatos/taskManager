package com.ipass.taskManager.dto;

import com.ipass.taskManager.model.Subtask;
import com.ipass.taskManager.model.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SubtaskResponseDto {
    private UUID id;
    private String titulo;
    private String descricao;
    private TaskStatus status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataConclusao;
    private UUID tarefaId;

    public static SubtaskResponseDto fromEntity(Subtask subtask) {
        if (subtask == null) {
            return null;
        }
        SubtaskResponseDto dto = new SubtaskResponseDto();
        dto.setId(subtask.getId());
        dto.setTitulo(subtask.getTitulo());
        dto.setDescricao(subtask.getDescricao());
        dto.setStatus(subtask.getStatus());
        dto.setDataCriacao(subtask.getDataCriacao());
        dto.setDataConclusao(subtask.getDataConclusao());
        if (subtask.getTarefaId() != null) {
            dto.setTarefaId(subtask.getTarefaId().getId());
        }
        return dto;
    }
}