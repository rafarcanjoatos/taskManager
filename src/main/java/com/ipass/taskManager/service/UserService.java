package com.ipass.taskManager.service;

import com.ipass.taskManager.dto.UserRequestDto;
import com.ipass.taskManager.model.User;
import com.ipass.taskManager.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(UUID id, UserRequestDto userDetailsDto) {
        User existingUser = getUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o id: " + id));

        existingUser.setNome(userDetailsDto.getNome());
        existingUser.setEmail(userDetailsDto.getEmail());  
        return userRepository.save(existingUser);
    }
}
