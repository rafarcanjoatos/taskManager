package com.ipass.taskManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipass.taskManager.dto.SubtaskRequestDto;
import com.ipass.taskManager.model.Subtask;
import com.ipass.taskManager.model.Task;
import com.ipass.taskManager.model.TaskStatus;
import com.ipass.taskManager.model.User;
import com.ipass.taskManager.repository.SubtaskRepository;
import com.ipass.taskManager.repository.TaskRepository;
import com.ipass.taskManager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SubtaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private SubtaskRepository subtaskRepository;

    private User testUser;
    private Task parentTask;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        subtaskRepository.deleteAll();
        taskRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setNome("Arcanjo");
        testUser.setEmail("arcanjo@example.com");
        testUser = userRepository.save(testUser);

        parentTask = new Task();
        parentTask.setTitulo("Parent Task");
        parentTask.setUsuario(testUser);
        parentTask = taskRepository.save(parentTask);
    }

    @Test
    @DisplayName("Teste de integração: Criar uma subtarefa")
    void createSubtask_withValidData_returnsCreated() throws Exception {
        SubtaskRequestDto subtaskRequest = new SubtaskRequestDto();
        subtaskRequest.setTitulo("Minha primeira subtarefa");

        mockMvc.perform(post("/tarefas/{tarefaId}/subtarefas", parentTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subtaskRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.titulo").value("Minha primeira subtarefa"))
                .andExpect(jsonPath("$.status").value(TaskStatus.PENDENTE.name()));
    }

    @Test
    @DisplayName("Teste de integração: Listar todas as subtarefas de uma tarefa")
    void getAllSubtasks_whenSubtasksExist_returnsSubtaskList() throws Exception {
        Subtask sub1 = new Subtask();
        sub1.setTitulo("Subtarefa 1");
        sub1.setTarefa(parentTask);
        subtaskRepository.save(sub1);

        Subtask sub2 = new Subtask();
        sub2.setTitulo("Subtarefa 2");
        sub2.setTarefa(parentTask);
        subtaskRepository.save(sub2);

        mockMvc.perform(get("/tarefas/{tarefaId}/subtarefas", parentTask.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].titulo").value("Subtarefa 1"))
                .andExpect(jsonPath("$[1].titulo").value("Subtarefa 2"));
    }

    @Test
    @DisplayName("Teste de integração: Atualizar uma subtarefa")
    void updateSubtask_withValidData_returnsOk() throws Exception {
        Subtask existingSubtask = new Subtask();
        existingSubtask.setTitulo("Título Original");
        existingSubtask.setTarefa(parentTask);
        existingSubtask = subtaskRepository.save(existingSubtask);

        SubtaskRequestDto updateRequest = new SubtaskRequestDto();
        updateRequest.setTitulo("Título Atualizado");

        mockMvc.perform(patch("/tarefas/{tarefaId}/subtarefas/{subtarefaId}", parentTask.getId(), existingSubtask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Título Atualizado"));
    }

    @Test
    @DisplayName("Teste de integração: Atualizar status de uma subtarefa")
    void updateSubtaskStatus_toConcluida_returnsOk() throws Exception {
        Subtask subtask = new Subtask();
        subtask.setTitulo("Subtarefa para concluir");
        subtask.setTarefa(parentTask);
        subtask.setStatus(TaskStatus.PENDENTE);
        Subtask savedSubtask = subtaskRepository.save(subtask);

        mockMvc.perform(patch("/tarefas/{tarefaId}/subtarefas/{subtarefaId}/status", parentTask.getId(), savedSubtask.getId())
                        .param("status", TaskStatus.CONCLUIDA.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(TaskStatus.CONCLUIDA.name()))
                .andExpect(jsonPath("$.dataConclusao").exists());
    }
}
