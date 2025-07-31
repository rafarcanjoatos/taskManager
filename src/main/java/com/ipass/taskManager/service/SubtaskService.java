package com.ipass.taskManager.service;

import com.ipass.taskManager.dto.SubtaskRequestDto;
import com.ipass.taskManager.exception.ResourceNotFoundException;
import com.ipass.taskManager.model.Subtask;
import com.ipass.taskManager.model.Task;
import com.ipass.taskManager.repository.SubtaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SubtaskService {

    private final SubtaskRepository subtaskRepository;
    private final TaskService taskService;

    public Subtask createSubtask(SubtaskRequestDto subtaskRequestDto) {
        Task parentTask = taskService.getTaskById(subtaskRequestDto.getTarefaId())
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa pai não encontrada com o id: " + subtaskRequestDto.getTarefaId()));
 
        Subtask subtask = new Subtask();
        subtask.setTitulo(subtaskRequestDto.getTitulo());
        subtask.setDescricao(subtaskRequestDto.getDescricao());
        subtask.setTarefaId(parentTask);
 
        return subtaskRepository.save(subtask);
    }

    @Transactional(readOnly = true)
    public Subtask getSubtaskById(UUID id) {
        return subtaskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subtask not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Subtask> getSubtasksByParentTaskId(UUID parentTaskId) {
        taskService.getTaskById(parentTaskId)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa pai não encontrada com o id: " + parentTaskId));
 
        return subtaskRepository.findByTarefaId(parentTaskId);
    }

    public Subtask updateSubtask(UUID id, SubtaskRequestDto subtaskDetailsDto) {
        Subtask existingSubtask = getSubtaskById(id);

        existingSubtask.setTitulo(subtaskDetailsDto.getTitulo());
        existingSubtask.setDescricao(subtaskDetailsDto.getDescricao());

        return subtaskRepository.save(existingSubtask);
    }

    public void deleteSubtask(UUID id) {
        Subtask subtask = getSubtaskById(id);
        subtaskRepository.delete(subtask);
    }
}