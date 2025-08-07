package com.ipass.taskManager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class TaskRequestDto {

    @NotBlank(message = "O título não pode estar em branco.")
    @Size(max = 100, message = "O título não pode exceder 100 caracteres.")
    private String titulo;

    @Size(max = 500, message = "A descrição não pode exceder 500 caracteres.")
    private String descricao;

    @NotNull(message = "O ID do usuário não pode ser nulo.")
    private UUID usuarioId;
}
