package com.ipass.taskManager.service;

import com.ipass.taskManager.dto.UserRequestDto;
import com.ipass.taskManager.model.User;
import com.ipass.taskManager.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    @Transactional
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User updateUser(UUID id, UserRequestDto userDetailsDto) {
        User existingUser = getUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o id: " + id));

        existingUser.setNome(userDetailsDto.getNome());
        existingUser.setEmail(userDetailsDto.getEmail());  
        return userRepository.save(existingUser);
    }

    @Transactional
    public User deleteUser(UUID id) {
        User existingUser = getUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o id: " + id));
        userRepository.delete(existingUser);
        return existingUser;
    }
}
