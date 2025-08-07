package com.ipass.taskManager.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Task extends AbstractTask {

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, orphanRemoval = true)
    @SuppressWarnings("unused") //Apenas para definir o relacionamento
    private List<Subtask> subtarefas;

}