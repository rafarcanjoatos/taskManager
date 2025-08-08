package com.ipass.taskManager.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ipass.taskManager.dto.TaskRequestDto;
import com.ipass.taskManager.model.Task;
import com.ipass.taskManager.model.TaskStatus;
import com.ipass.taskManager.model.User;
import com.ipass.taskManager.repository.SubtaskRepository;
import com.ipass.taskManager.repository.TaskRepository;
import com.ipass.taskManager.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final SubtaskRepository subtaskRepository;
    private final UserRepository userRepository;

    
    @Transactional
    public Task createTask(TaskRequestDto taskRequestDto) {
        validateTaskRequest(taskRequestDto);
        
        UUID usuarioId = taskRequestDto.getUsuarioId();

        Task task = new Task();
        task.setTitulo(taskRequestDto.getTitulo());
        task.setDescricao(taskRequestDto.getDescricao());
        User user = userRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + usuarioId));
        task.setUsuario(user);

        return taskRepository.save(task);
    }


    @Transactional(readOnly = true)
    public Task getTaskById(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada com o id: " + id));
    }


    @Transactional(readOnly = true)
    public List<Task> getAllTasks(TaskStatus status) {
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
        if (status == null || (status != TaskStatus.PENDENTE && status != TaskStatus.EM_ANDAMENTO && status != TaskStatus.CONCLUIDA && status != TaskStatus.EXCLUIDA)){
            throw new ValidationException("O status da tarefa é obrigatório.");
        }

        Task existingTask = getTaskById(id);
                
        if (status == TaskStatus.CONCLUIDA || status == TaskStatus.EXCLUIDA) {
            boolean hasPendingSubtasks = subtaskRepository.findByTarefaId_Id(id)
                    .stream()
                    .anyMatch(subtask -> subtask.getStatus() != TaskStatus.CONCLUIDA && subtask.getStatus() != TaskStatus.EXCLUIDA);

            if (hasPendingSubtasks && status == TaskStatus.CONCLUIDA) {
                throw new IllegalStateException("A tarefa não pode ser concluída pois possui subtarefas pendentes.");
            }

            if (hasPendingSubtasks && status == TaskStatus.EXCLUIDA) {
                throw new IllegalStateException("A tarefa não pode ser excluída pois possui subtarefas pendentes.");
            }
        }

        existingTask.setStatus(status);
        
        if (status == TaskStatus.CONCLUIDA || status == TaskStatus.EXCLUIDA) {
            existingTask.setDataConclusao(LocalDateTime.now());
        } else {
            existingTask.setDataConclusao(null);
        }

        return taskRepository.save(existingTask);
    }


    @Transactional
    public Task deleteTask(UUID id) {
        Task task = getTaskById(id);
        updateTaskStatus(id, TaskStatus.EXCLUIDA);

        taskRepository.delete(task);

        return task;
    }


    private void validateTaskRequest(TaskRequestDto dto) {
        if (dto.getTitulo() == null || dto.getTitulo().trim().isEmpty()) {
            throw new ValidationException("O título da tarefa é obrigatório.");
        }

        if (dto.getTitulo() != null && dto.getTitulo().length() > 100) {
            throw new ValidationException("O título da tarefa não pode exceder 100 caracteres.");
        }

        if (dto.getUsuarioId() == null) { 
            throw new ValidationException("O ID do usuário é obrigatório.");
        }        

        if (dto.getDescricao() != null && dto.getDescricao().length() > 500) {
            throw new ValidationException("A descrição da tarefa não pode exceder 500 caracteres.");
        }   
    }
}
