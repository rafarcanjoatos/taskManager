# Gerenciador de Tarefas - Desafio Técnico iPaaS

Este projeto implementa uma API RESTful para gerenciamento de tarefas e subtarefas, conforme o desafio técnico da iPaaS para a vaga de Desenvolvedor Backend Java Pleno.

## Tecnologias Utilizadas

* **Java 17+**: Linguagem de programação principal.
* **Spring Boot 3**: Framework para construção de aplicações Java.
* **Spring Data JPA**: Para persistência de dados com o banco H2.
* **Hibernate**: Implementação de JPA.
* **H2 Database**: Banco de dados em memória, ideal para desenvolvimento e testes.
* **Lombok**: Biblioteca para reduzir código boilerplate (getters, setters, construtores, etc.).
* **Jakarta Validation (Bean Validation)**: Para validação de dados de entrada.
* **JUnit 5 & Mockito**: Para testes unitários e de integração.
* **Maven Surefire Plugin**: Para execuçã de testes e geração de relatórios
* **Springdoc OpenAPI (Swagger UI)**: Para documentação interativa da API.
* **Docker & Docker Compose**: Para containerização e orquestração do ambiente de desenvolvimento.
* **Logback (SLF4J)**: Para gerenciamento de logs.
* **Maven**: Ferramenta de gerenciamento e automação de builds.


## Estrutura do Projeto

O projeto segue uma arquitetura baseada em camadas, organizada por responsabilidades:

src/main/java/com/ipaas/taskManager  
├── controller       // Endpoints RESTful da API  
├── configuration    // Configurações da aplicação (Ex: Swagger, DatabaseSeeder)  
├── dto              // Objetos de Transferência de Dados (Requisição e Resposta)  
├── exception        // Classes para tratamento de exceções customizadas  
├── model            // Entidades do domínio (User, Task, Subtask)  
├── repository       // Interfaces de acesso a dados (Spring Data JPA)  
├── service          // Camada de lógica de negócio, validação e regras das entidades
└── TaskManagerApplication.java // Classe principal da aplicação  


## Como Rodar o Projeto

Você pode rodar a aplicação de duas formas:


### 1. Com Maven (sem Docker)

Certifique-se de ter o Java 17+ e o Maven instalados.

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/rafarcanjoatos/taskManager]
    cd taskManager
    ```
2.  **Compile o projeto:**
    ```bash
    ./mvnw clean install
    ```
3.  **Execute a aplicação:**
    ```bash
    ./mvnw spring-boot:run
    ```
    A aplicação estará disponível em `http://localhost:8080`.


### 2. Com Docker e Docker Compose (Recomendado)

Esta é a forma mais fácil de subir a aplicação e o banco de dados. Certifique-se de ter o Docker e Docker Compose instalados.

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/rafarcanjoatos/taskManager]
    cd taskManager
    ```
2.  **[Docker Compose] Build e suba os serviços:**
    ```bash
    docker-compose up --build
    ```
    A aplicação estará disponível em `http://localhost:8080`.

3.  **[Docker] Construa e suba os serviços:**
    ```bash
    docker build -t task-manager-app .
    docker run -p 8080:8080 --name taskmanager-container task-manager-app
    ```
    A aplicação estará disponível em `http://localhost:8080`.


## 3. Endpoints da API

Após iniciar a aplicação, você pode acessar:

* **Console H2 Database:** Para inspecionar o banco de dados em memória.
    * URL: `http://localhost:8080/h2-console`
    * Credenciais (padrão do `application.properties`):
        * JDBC URL: `jdbc:h2:file:./data/task_manager_db`
        * User Name: `sa`
        * Password: (vazio)
* **Swagger UI (Documentação da API):** Para explorar e testar os endpoints interativamente.
    * URL: `http://localhost:8080/swagger-ui.html`


### Exemplos de Endpoints

**Usuários:**

*   **`GET /usuarios`**
    *   **Descrição**: Lista todos os usuários cadastrados.

*   **`GET /usuarios/{id}`**
    *   **Descrição**: Busca um usuário por ID.

*   **`POST /usuarios`**
    * **Descrição**: Cria um novo usuário.

*   **`PATCH /usuarios/{id}`**
    *   **Descrição**: Atualiza os dados de um usuário existente.

*   **`DELETE /usuarios/{id}`**
    *   **Descrição**: Remove um usuário.


**Tarefas:**

*   **`GET /tarefas`**
    *   **Descrição**: Lista tarefas, com a opção de filtrar por status.

*   **`GET /tarefas/{id}`**
    *   **Descrição**: Busca uma tarefa específica por ID.

*   **`POST /tarefas`**
    * **Descrição**: Cria uma nova tarefa para um usuário.

*   **`PATCH /tarefas/{id}`**
    *   **Descrição**: Atualiza o título e a descrição de uma tarefa.
    
*   **`PATCH /tarefas/{id}/status`**
    * **Descrição**: Atualiza o status de uma tarefa.
    * **Observação**: Uma tarefa só pode ser concluída se todas as suas subtarefas estiverem com status `CONCLUIDA`.

*   **`DELETE /tarefas/{id}`**
    *   **Descrição**: Remove uma tarefa, se não houver subtarefas relacionadas.



**Subtarefas:**

*   **`GET /tarefas/{tarefaId}/subtarefas`**
    *   **Descrição**: Lista todas as subtarefas de uma tarefa.

*   **`POST /tarefas/{tarefaId}/subtarefas`**
    * **Descrição**: Cria uma nova subtarefa para uma tarefa específica.

*   **`GET /tarefas/{tarefaId}/subtarefas/{subtarefaId}`**
    *   **Descrição**: Busca uma subtarefa específica por ID.

*   **`PATCH /tarefas/{tarefaId}/subtarefas/{subtarefaId}`**
    *   **Descrição**: Atualiza o título e a descrição de uma subtarefa.

*   **`PATCH /tarefas/{tarefaId}/subtarefas/{subtarefaId}/status`**
    * **Descrição**: Atualiza o status de uma subtarefa.
    * **Observação**: A alteração de status da subtarefa não afeta automaticamente a tarefa principal, mas impede sua conclusão enquanto houver subtarefas pendentes.
    
*   **`DELETE /subtarefas/{id}`**
    *   **Descrição**: Remove uma subtarefa.


---
