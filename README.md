# Gerenciador de Tarefas - Desafio Técnico iPaaS

Este projeto implementa uma API RESTful para gerenciamento de tarefas e subtarefas, conforme o desafio técnico da iPaaS para a vaga de Desenvolvedor Backend Java Pleno.

## Tecnologias Utilizadas

* **Java 17+**: Linguagem de programação principal.
* **Spring Boot 3**: Framework para construção de aplicações Java.
* **Spring Data JPA**: Para persistência de dados com o banco H2.
* **H2 Database**: Banco de dados em memória, ideal para desenvolvimento e testes.
* **Lombok**: Biblioteca para reduzir código boilerplate (getters, setters, construtores, etc.).
* **Jakarta Validation (Bean Validation)**: Para validação de dados de entrada.
* **JUnit 5 & Mockito**: Para testes unitários e de integração.
* **Springdoc OpenAPI (Swagger UI)**: Para documentação interativa da API.

## Estrutura do Projeto

O projeto segue uma arquitetura baseada em camadas, organizada por responsabilidades:

src/main/java/com/ipaas/taskManager  
├── controller       // Endpoints RESTful da API  
├── configuration    // Configurações da aplicação (Ex: Swagger)  
├── dto              // Objetos de Transferência de Dados (Requisição e Resposta)  
├── exception        // Classes para tratamento de exceções customizadas  
├── model            // Entidades do domínio (User, Task, Subtask)  
├── repository       // Interfaces de acesso a dados (Spring Data JPA)  
├── service          // Camada de lógica de negócio e regras das entidades  
└── TaskManagerApplication.java // Classe principal da aplicação  


## Como Rodar o Projeto

Você pode rodar a aplicação de duas formas:

### 1. Com Maven (sem Docker)

Certifique-se de ter o Java 17+ e o Maven instalados.

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/SEU_USUARIO/SEU_REPOSITORIO.git](https://github.com/SEU_USUARIO/SEU_REPOSITORIO.git) # Substitua pelo seu usuário e nome do repositório
    cd SEU_REPOSITORIO
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
    git clone [https://github.com/SEU_USUARIO/SEU_REPOSITORIO.git](https://github.com/SEU_USUARIO/SEU_REPOSITORIO.git) # Substitua pelo seu usuário e nome do repositório
    cd SEU_REPOSITORIO
    ```
2.  **Construa e suba os serviços:**
    ```bash
    docker-compose up --build
    ```
    A aplicação estará disponível em `http://localhost:8080`.

## Endpoints da API

Após iniciar a aplicação, você pode acessar:

* **Console H2 Database:** Para inspecionar o banco de dados em memória.
    * URL: `http://localhost:8080/h2-console`
    * Credenciais (padrão do `application.properties`):
        * JDBC URL: `jdbc:h2:mem:desafio_db`
        * User Name: `sa`
        * Password: (vazio)
* **Swagger UI (Documentação da API):** Para explorar e testar os endpoints interativamente.
    * URL: `http://localhost:8080/swagger-ui.html`


### Exemplos de Endpoints

**Usuários:**

* **`POST /usuarios`**
    * **Descrição**: Cria um novo usuário.
    * **Corpo da Requisição (JSON)**:
        ```json
        {
          "nome": "João da Silva",
          "email": "joao.silva@example.com"
        }
        ```
    * **Resposta (201 Created)**:
        ```json
        {
          "id": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
          "nome": "João da Silva",
          "email": "joao.silva@example.com"
        }
        ```
* **`GET /usuarios/{id}`**
    * **Descrição**: Busca um usuário por ID.
    * **Exemplo**: `/usuarios/a1b2c3d4-e5f6-7890-1234-567890abcdef`

**Tarefas:**

* **`POST /tarefas`**
    * **Descrição**: Cria uma nova tarefa para um usuário.
    * **Corpo da Requisição (JSON)**:
        ```json
        {
          "titulo": "Implementar funcionalidade X",
          "descricao": "Detalhes da funcionalidade...",
          "usuarioId": "a1b2c3d4-e5f6-7890-1234-567890abcdef"
        }
        ```

* **`GET /tarefas?status=PENDENTE`**
    * **Descrição**: Lista tarefas filtradas por status.
    * **Exemplo**: `/tarefas` ou `/tarefas?status=EM_ANDAMENTO`

* **`PATCH /tarefas/{id}/status`**
    * **Descrição**: Atualiza o status de uma tarefa.
    * **Corpo da Requisição (JSON)**:
        ```json
        {
          "newStatus": "CONCLUIDA"
        }
        ```
    * **Observação**: Uma tarefa só pode ser concluída se todas as suas subtarefas estiverem com status `CONCLUIDA`.

**Subtarefas:**

* **`POST /tarefas/{tarefaId}/subtarefas`**
    * **Descrição**: Cria uma nova subtarefa para uma tarefa específica.
    * **Exemplo**: `/tarefas/f1e2d3c4-b5a6-9876-5432-10fedcba9876/subtarefas`
    * **Corpo da Requisição (JSON)**:
        ```json
        {
          "titulo": "Criar endpoint de login",
          "descricao": "Detalhes da subtarefa..."
        }
        ```

* **`GET /tarefas/{tarefaId}/subtarefas`**
    * **Descrição**: Lista todas as subtarefas de uma tarefa.
    * **Exemplo**: `/tarefas/f1e2d3c4-b5a6-9876-5432-10fedcba9876/subtarefas`

* **`PATCH /subtarefas/{id}/status`**
    * **Descrição**: Atualiza o status de uma subtarefa.
    * **Corpo da Requisição (JSON)**:
        ```json
        {
          "newStatus": "CONCLUIDA"
        }
        ```
    * **Observação**: A alteração de status da subtarefa não afeta automaticamente a tarefa principal, mas impede sua conclusão enquanto houver subtarefas pendentes.

---
