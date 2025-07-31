package com.ipass.taskManager.dto;

import com.ipass.taskManager.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDto {
    @NotBlank(message = "O nome não pode estar em branco.")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
    private String nome;

    @Email(message = "O formato do email é inválido.")
    @NotBlank(message = "O email não pode estar em branco.")
    private String email;

    public User toEntity() {
        User user = new User();
        user.setNome(this.nome);
        user.setEmail(this.email);
        return user;
    }
}
