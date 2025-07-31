package com.ipass.taskManager.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ipass.taskManager.model.Subtask;

@Repository
public interface SubtaskRepository extends JpaRepository<Subtask, UUID> {

    List<Subtask> findByTarefaId(UUID parentTaskId);
}