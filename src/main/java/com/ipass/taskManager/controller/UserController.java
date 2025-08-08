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
import org.springframework.web.bind.annotation.RestController;

import com.ipass.taskManager.dto.UserRequestDto;
import com.ipass.taskManager.dto.UserResponseDto;
import com.ipass.taskManager.model.User;
import com.ipass.taskManager.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Usuários", description = "Operações relacionadas a usuários na aplicação.")
@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UserController { 
    private final UserService userService; 

    @Operation(
        summary = "Cria um usuário",
        description = "Cria um novo usuário com os detalhes fornecidos"
    )
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        User createdUser = userService.createUser(userRequestDto.toEntity());
        return new ResponseEntity<>(UserResponseDto.fromEntity(createdUser), HttpStatus.CREATED);
    }

    @Operation(
        summary = "Busca um usuário por ID",
        description = "Busca apenas um usuário por ID e retorna seus detalhes"
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID id) {
        return userService.getUserById(id)
                .map(UserResponseDto::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Busca todos os usuário",
        description = "Busca todos os usuário e retorna seus detalhes"
    )
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponseDto> dtos = users.stream()
                .map(UserResponseDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(
        summary = "Atualiza um usuário por ID",
        description = "Atualiza um usuário por id e retorna seus detalhes"
    )
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUserById(@PathVariable UUID id, @Valid @RequestBody UserRequestDto userRequestDto) {
        User updatedUser = userService.updateUser(id, userRequestDto);
        return ResponseEntity.ok(UserResponseDto.fromEntity(updatedUser));
    }
}