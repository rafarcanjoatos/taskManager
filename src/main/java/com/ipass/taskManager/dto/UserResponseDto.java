package com.ipass.taskManager.dto;

import java.util.UUID;

import com.ipass.taskManager.model.User;

import lombok.Data;

@Data
public class UserResponseDto {    
    private UUID id;
    private String nome;
    private String email;

    public static UserResponseDto fromEntity(User user) {
        if (user == null) {
            return null;
        }

        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setNome(user.getNome());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
