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
import com.ipass.taskManager.repository.SubtaskRepository;
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

    @Autowired
    private SubtaskRepository subtaskRepository;

    private User testUser;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        subtaskRepository.deleteAll();
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
        taskRequest.setTitulo("Tarefa 0");
        taskRequest.setDescricao("Descrição 0");
        taskRequest.setUsuarioId(testUser.getId());


        mockMvc.perform(post("/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.titulo").value("Tarefa 0"))
                .andExpect(jsonPath("$.descricao").value("Descrição 0"));
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
    @DisplayName("Teste de integração: Obter tarefas filtrando por status")
    void getAllTasks_whenFilteringByStatus_returnsFilteredList() throws Exception {
        Task task = new Task();
        task.setTitulo("Tarefa Pendente");
        task.setUsuario(testUser);
        task.setStatus(TaskStatus.PENDENTE);
        Task savedTask = taskRepository.save(task);

        mockMvc.perform(get("/tarefas")
                        .param("status", TaskStatus.PENDENTE.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(savedTask.getId().toString()))
                .andExpect(jsonPath("$[0].titulo").value("Tarefa Pendente"))
                .andExpect(jsonPath("$[0].status").value(TaskStatus.PENDENTE.name()));
    }
    
    @Test
    @DisplayName("Teste de integração: Obter todas as tarefas")
    void getAllTasks_whenTasksExist_returnsTaskList() throws Exception {
        Task task1 = new Task();
        task1.setTitulo("Tarefa 1");
        task1.setUsuario(testUser);
        taskRepository.save(task1);

        Task task2 = new Task();
        task2.setTitulo("Tarefa 2");
        task2.setUsuario(testUser);
        taskRepository.save(task2);

        mockMvc.perform(get("/tarefas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
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