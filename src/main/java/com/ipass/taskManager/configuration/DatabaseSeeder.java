package com.ipass.taskManager.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.ipass.taskManager.model.Subtask;
import com.ipass.taskManager.model.Task;
import com.ipass.taskManager.model.User;
import com.ipass.taskManager.repository.SubtaskRepository;
import com.ipass.taskManager.repository.TaskRepository;
import com.ipass.taskManager.repository.UserRepository;

@Component
@Profile("dev")
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final SubtaskRepository subtaskRepository;


    public DatabaseSeeder(UserRepository userRepository, TaskRepository taskRepository, SubtaskRepository subtaskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.subtaskRepository = subtaskRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0 && taskRepository.count() == 0 && subtaskRepository.count() == 0) {
            User user = new User();
            user.setNome("Admin User");
            user.setEmail("admin@example.com");
            userRepository.save(user);

            System.out.println("Usuário inicial criado: " + user.getNome());
       

            Task tarefa = new Task();
            tarefa.setTitulo("Tarefa");
            tarefa.setUsuario(user);
            tarefa.setDescricao("Descrição da tarefa");
            tarefa = taskRepository.save(tarefa);

            System.out.println("Tarefa criada: " + tarefa.getTitulo());
    

            Subtask subtarefa1 = new Subtask();
            subtarefa1.setTitulo("Subtarefa 1");
            subtarefa1.setTarefa(tarefa);
            subtarefa1.setDescricao("Descrição da subtarefa 1");
            subtaskRepository.save(subtarefa1);
            
            System.out.println("Subtarefas criadas: " + subtarefa1.getTitulo());


            Subtask subtarefa2 = new Subtask();
            subtarefa2.setTitulo("Subtarefa 2");
            subtarefa2.setTarefa(tarefa);
            subtarefa2.setDescricao("Descrição da subtarefa 2");
            subtaskRepository.save(subtarefa2);
            
            System.out.println("Subtarefas criadas: " + subtarefa2.getTitulo());
        }
    }
}