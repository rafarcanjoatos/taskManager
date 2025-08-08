package com.ipass.taskManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipass.taskManager.dto.TaskRequestDto;
import com.ipass.taskManager.model.Task;
import com.ipass.taskManager.model.TaskStatus;
import com.ipass.taskManager.model.User;
import com.ipass.taskManager.repository.TaskRepository;
import com.ipass.taskManager.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    private User testUser;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setNome("Arcanjo");
        testUser.setEmail("test@example.com");
        testUser = userRepository.save(testUser);
    }

    @Test
    @DisplayName("Teste de integração: Criar uma tarefa")
    void createTask_withValidData_returnsCreated() throws Exception {
        TaskRequestDto taskRequest = new TaskRequestDto();
        taskRequest.setTitulo("Tarefa 1");
        taskRequest.setDescricao("Descrição 1");
        taskRequest.setUsuarioId(testUser.getId());


        mockMvc.perform(post("/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.titulo").value("Tarefa 1"))
                .andExpect(jsonPath("$.descricao").value("Descrição 1"));
    }


    @Test
    @DisplayName("Teste de integração: Falha ao criar tarefa sem título")
    void createTask_withMissingTitle_returnsBadRequest() throws Exception {
        TaskRequestDto taskRequest = new TaskRequestDto();
        taskRequest.setDescricao("Descrição sem título");
        taskRequest.setUsuarioId(testUser.getId());

        mockMvc.perform(post("/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("Teste de integração: Obter uma tarefa por ID")
    void getTaskById_whenTaskExists_returnsTask() throws Exception {
        Task task = new Task();
        task.setTitulo("Tarefa para buscar");
        task.setUsuario(testUser);
        Task savedTask = taskRepository.save(task);

        mockMvc.perform(get("/tarefas/{id}", savedTask.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedTask.getId().toString()))
                .andExpect(jsonPath("$.titulo").value("Tarefa para buscar"));
    }


    @Test
    @DisplayName("Teste de integração: Atualizar uma tarefa com PUT")
    void updateTask_withValidData_returnsOk() throws Exception {
        Task task = new Task();
        task.setTitulo("Tarefa Original");
        task.setUsuario(testUser);
        Task savedTask = taskRepository.save(task);

        TaskRequestDto updateRequest = new TaskRequestDto();
        updateRequest.setTitulo("Tarefa Atualizada");
        updateRequest.setDescricao("Descrição Atualizada");
        updateRequest.setUsuarioId(testUser.getId());

        mockMvc.perform(patch("/tarefas/{id}", savedTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Tarefa Atualizada"))
                .andExpect(jsonPath("$.descricao").value("Descrição Atualizada"));
    }

    @Test
    @DisplayName("Teste de integração: Completar uma tarefa")
    void completeTask_whenTaskExists_returnsOk() throws Exception {
        Task task = new Task();
        task.setTitulo("Tarefa a completar");
        task.setUsuario(testUser);
        task.setStatus(TaskStatus.PENDENTE);
        Task savedTask = taskRepository.save(task);

        mockMvc.perform(patch("/tarefas/{id}/status", savedTask.getId())
                        .param("status", TaskStatus.CONCLUIDA.name()))
                .andExpect(status().isOk());


        mockMvc.perform(get("/tarefas/{id}", savedTask.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONCLUIDA"))
                .andExpect(jsonPath("$.dataConclusao").isNotEmpty());
    }

}