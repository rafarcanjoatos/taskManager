package com.ipass.taskManager.configuration;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Task Manager API",
        version = "2.0.0",
        description = "API RESTful para gerenciar tarefas, subtarefas e usuários, em um sistema de controle de atividades.",
        contact = @Contact(
            name = "Rafael Arcanjo",
            url = "https://github.com/rafarcanjoatos/",
            email = "rafael.arcanjo@totvs.com.br"
        )
    )
)
public class OpenApiConfig {
}