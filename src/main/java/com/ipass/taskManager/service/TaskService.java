package com.ipass.taskManager.service;

import com.ipass.taskManager.dto.TaskRequestDto;
import com.ipass.taskManager.model.Task;
import com.ipass.taskManager.model.TaskStatus;
import com.ipass.taskManager.model.User;
import com.ipass.taskManager.repository.SubtaskRepository;
import com.ipass.taskManager.repository.TaskRepository;
import com.ipass.taskManager.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final SubtaskRepository subtaskRepository;

    
    @Transactional
    public Task createTask(TaskRequestDto taskRequestDto) {
        validateTaskRequest(taskRequestDto);
        
        User user = userRepository.findById(taskRequestDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o id: " + taskRequestDto.getUserId()));

        Task task = new Task();
        task.setTitulo(taskRequestDto.getTitulo());
        task.setDescricao(taskRequestDto.getDescricao());
        task.setUser(user);

        return taskRepository.save(task);
    }


    @Transactional(readOnly = true)
    public Task getTaskById(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada com o id: " + id));
    }


    @Transactional(readOnly = true)
    public List<Task> getAllTasks(@RequestParam(required = false) TaskStatus status) {
        if (status != null) {
            return taskRepository.findByStatus(status);
        }
                
        return taskRepository.findAll();
    }


    @Transactional
    public Task updateTask(UUID id, TaskRequestDto taskRequestDto) {
        validateTaskRequest(taskRequestDto);

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada com o id: " + id));

        existingTask.setTitulo(taskRequestDto.getTitulo());
        existingTask.setDescricao(taskRequestDto.getDescricao());

        return taskRepository.save(existingTask);
    }


    @Transactional
    public Task updateTaskStatus(UUID id, TaskStatus status) {
        if (status == null || (status != TaskStatus.PENDENTE && status != TaskStatus.EM_ANDAMENTO && status != TaskStatus.CONCLUIDA)){
            throw new ValidationException("O status da tarefa é obrigatório.");
        }

        Task existingTask = getTaskById(id);
                
        if (status == TaskStatus.CONCLUIDA) {
            boolean hasPendingSubtasks = subtaskRepository.findByTarefaId_Id(id)
                    .stream()
                    .anyMatch(subtask -> subtask.getStatus() != TaskStatus.CONCLUIDA);

            if (hasPendingSubtasks) {
                throw new IllegalStateException("A tarefa não pode ser concluída pois possui subtarefas pendentes.");
            }
        }

        existingTask.setStatus(status);
        
        if (status == TaskStatus.CONCLUIDA) {
            existingTask.setDataConclusao(LocalDateTime.now());
        } else {
            existingTask.setDataConclusao(null);
        }

        return taskRepository.save(existingTask);
    }


    private void validateTaskRequest(TaskRequestDto dto) {
        if (dto.getTitulo() == null || dto.getTitulo().trim().isEmpty()) {
            throw new ValidationException("O título da tarefa é obrigatório.");
        }

        if (dto.getTitulo() != null && dto.getTitulo().length() > 100) {
            throw new ValidationException("O título da tarefa não pode exceder 100 caracteres.");
        }

        if (dto.getUserId() == null) { 
            throw new ValidationException("O ID do usuário é obrigatório.");
        }        

        if (dto.getDescricao() != null && dto.getDescricao().length() > 500) {
            throw new ValidationException("A descrição da tarefa não pode exceder 500 caracteres.");
        }   
    }
}
