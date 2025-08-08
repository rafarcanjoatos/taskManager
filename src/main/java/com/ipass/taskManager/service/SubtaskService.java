package com.ipass.taskManager.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ipass.taskManager.dto.SubtaskRequestDto;
import com.ipass.taskManager.exception.ResourceNotFoundException;
import com.ipass.taskManager.model.Subtask;
import com.ipass.taskManager.model.Task;
import com.ipass.taskManager.model.TaskStatus;
import com.ipass.taskManager.repository.SubtaskRepository;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SubtaskService {

    private final SubtaskRepository subtaskRepository;
    private final TaskService taskService;
    
    
    @Transactional
    public Subtask createSubtask(UUID parentTaskId, SubtaskRequestDto subtaskRequestDto) {
        validateSubtaskRequest(subtaskRequestDto);
 
        Subtask subtask = new Subtask();
        subtask.setTitulo(subtaskRequestDto.getTitulo());
        subtask.setDescricao(subtaskRequestDto.getDescricao());
        Task parentTask = taskService.getTaskById(parentTaskId);
        subtask.setTarefa(parentTask);
 
        return subtaskRepository.save(subtask);
    }


    @Transactional(readOnly = true)
    public Subtask getSubtaskById(UUID id) {
        return subtaskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subtask not found with id: " + id));
    }


    @Transactional(readOnly = true)
    public List<Subtask> getSubtasksByParentTaskId(UUID parentTaskId) {
        taskService.getTaskById(parentTaskId);
 
        return subtaskRepository.findByTarefaId_Id(parentTaskId);
    }


    public Subtask updateSubtaskById(UUID id, SubtaskRequestDto subtaskRequestDto) {
        validateSubtaskRequest(subtaskRequestDto);

        Subtask existingSubtask = getSubtaskById(id);

        existingSubtask.setTitulo(subtaskRequestDto.getTitulo());
        existingSubtask.setDescricao(subtaskRequestDto.getDescricao());

        return subtaskRepository.save(existingSubtask);
    }


    @Transactional
    public Subtask updateSubtaskStatus(UUID id, TaskStatus status) {
        if (status == null || (status != TaskStatus.PENDENTE && status != TaskStatus.EM_ANDAMENTO && status != TaskStatus.CONCLUIDA)){
            throw new ValidationException("O status da tarefa é obrigatório.");
        }

        Subtask existingSubtask = getSubtaskById(id);

        existingSubtask.setStatus(status);
        if (status == TaskStatus.CONCLUIDA) {
            existingSubtask.setDataConclusao(LocalDateTime.now());
        } else {
            existingSubtask.setDataConclusao(null);
        }

        return subtaskRepository.save(existingSubtask);
    }
    

    public void deleteSubtask(UUID id) {
        Subtask subtask = getSubtaskById(id);
        subtaskRepository.delete(subtask);
    }


    private void validateSubtaskRequest(SubtaskRequestDto dto) {
        if (dto.getTitulo() == null || dto.getTitulo().trim().isEmpty()) {
            throw new ValidationException("O título da tarefa é obrigatório.");
        }

        if (dto.getTitulo() != null && dto.getTitulo().length() > 100) {
            throw new ValidationException("O título da tarefa não pode exceder 100 caracteres.");
        }

        if (dto.getDescricao() != null && dto.getDescricao().length() > 500) {
            throw new ValidationException("A descrição da tarefa não pode exceder 500 caracteres.");
        }   
    }
}