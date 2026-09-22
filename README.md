# DevShowcase API

API REST desenvolvida em Java com Spring Boot para cadastro, gerenciamento e compartilhamento de projetos de desenvolvedores.

O projeto foi desenvolvido como atividade acadêmica da Universidade Aberta do Piauí (UAPI/UESPI), com foco em desenvolvimento Back-End, persistência de dados, regras de negócio, tratamento global de exceções, documentação com Swagger/OpenAPI e deploy em nuvem.

---

## Sobre o projeto

O **DevShowcase** é uma API REST que permite cadastrar desenvolvedores, tecnologias, projetos e avaliações.

A aplicação permite que um perfil de desenvolvedor possua vários projetos, que cada projeto utilize várias tecnologias e que cada projeto receba avaliações dos usuários.

Além das operações básicas de cadastro e consulta, a API possui funcionalidades avançadas, como:

- Cadastro de avaliações com notas de 1 a 5;
- Cálculo automático da média de avaliação dos projetos;
- Sistema de curtidas/upvotes;
- Filtragem de projetos por tecnologia;
- Paginação dos resultados;
- Tratamento global de exceções;
- Validação dos dados recebidos;
- Documentação interativa com Swagger/OpenAPI;
- Banco PostgreSQL hospedado em nuvem;
- Deploy da aplicação no Render;
- Configuração através de variáveis de ambiente.

---

# Objetivos

O projeto teve como principais objetivos:

- Desenvolver uma API REST utilizando Java e Spring Boot;
- Aplicar arquitetura em camadas;
- Utilizar Spring Data JPA para persistência dos dados;
- Trabalhar com banco de dados PostgreSQL;
- Utilizar DTOs para entrada e saída de dados;
- Aplicar validações utilizando Bean Validation;
- Implementar relacionamentos entre entidades;
- Criar regras de negócio na camada de serviço;
- Implementar tratamento global de exceções;
- Documentar a API com Swagger/OpenAPI;
- Utilizar Docker;
- Realizar deploy em ambiente de nuvem;
- Trabalhar com Git e GitHub durante o desenvolvimento.

---

# Tecnologias utilizadas

## Back-End

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- Bean Validation
- Lombok
- Maven

## Banco de dados

- PostgreSQL

## Documentação

- Swagger UI
- OpenAPI

## DevOps e Deploy

- Docker
- Git
- GitHub
- Render

---

# Arquitetura

O projeto utiliza uma arquitetura em camadas, separando as responsabilidades da aplicação.

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

### Controller

Responsável por receber as requisições HTTP, chamar os serviços e retornar as respostas da API.

### Service

Responsável pelas regras de negócio da aplicação.

Exemplos:

- cálculo da média das avaliações;
- incremento de upvotes;
- validação da existência de recursos;
- associação de projetos com perfis;
- associação de projetos com tecnologias.

### Repository

Responsável pelo acesso aos dados utilizando Spring Data JPA.

### Entity

Representa as tabelas e os relacionamentos do banco de dados.

### DTO

Utilizado para transportar os dados entre a API e o cliente, evitando expor diretamente as entidades da aplicação.

---

# Estrutura do projeto

```text
src
└── main
    └── java
        └── com
            └── devshowcase
                ├── config
                │   └── OpenApiConfig.java
                │
                ├── controller
                │   ├── FeedbackController.java
                │   ├── ProfileController.java
                │   ├── ProjectController.java
                │   └── TechnologyController.java
                │
                ├── dto
                │   ├── FeedbackRequestDTO.java
                │   ├── FeedbackResponseDTO.java
                │   ├── ProfileRequestDTO.java
                │   ├── ProfileResponseDTO.java
                │   ├── ProjectRequestDTO.java
                │   ├── ProjectResponseDTO.java
                │   ├── TechnologyRequestDTO.java
                │   └── TechnologyResponseDTO.java
                │
                ├── entity
                │   ├── Feedback.java
                │   ├── Profile.java
                │   ├── Project.java
                │   └── Technology.java
                │
                ├── exception
                │   ├── GlobalExceptionHandler.java
                │   └── ResourceNotFoundException.java
                │
                ├── repository
                │   ├── FeedbackRepository.java
                │   ├── ProfileRepository.java
                │   ├── ProjectRepository.java
                │   └── TechnologyRepository.java
                │
                ├── service
                │   ├── FeedbackService.java
                │   ├── ProfileService.java
                │   ├── ProjectService.java
                │   └── TechnologyService.java
                │
                └── DevshowcaseApplication.java
```

---

# Modelagem do domínio

A aplicação possui quatro entidades principais:

- Profile
- Project
- Technology
- Feedback

---

# Relacionamentos

```text
Profile
   │
   │ 1:N
   ▼
Project
   │
   ├───────────────┐
   │               │
   │ N:N           │ 1:N
   ▼               ▼
Technology       Feedback
```

## Profile → Project

Um perfil pode possuir vários projetos.

```text
Profile 1 : N Project
```

## Project → Technology

Um projeto pode utilizar várias tecnologias e uma tecnologia pode estar presente em vários projetos.

```text
Project N : N Technology
```

## Project → Feedback

Um projeto pode receber várias avaliações.

```text
Project 1 : N Feedback
```

---

# Entidades

## Profile

Representa o perfil do desenvolvedor.

Principais atributos:

```text
id
name
bio
githubUrl
linkedinUrl
```

Um perfil pode possuir vários projetos.

---

## Project

Representa um projeto desenvolvido por um perfil.

Principais atributos:

```text
id
title
description
githubUrl
projectUrl
averageRating
upvotes
```

Além disso, possui relacionamento com:

- Profile;
- Technology;
- Feedback.

---

## Technology

Representa uma tecnologia utilizada nos projetos.

Principais atributos:

```text
id
name
```

Possui relacionamento N:N com Project.

---

## Feedback

Representa uma avaliação realizada em um projeto.

Principais atributos:

```text
id
rating
comment
```

Possui relacionamento com Project.

---

# DTOs

A aplicação utiliza DTOs para entrada e saída de dados.

## Request DTOs

Utilizados para receber informações enviadas pelo cliente.

```text
ProfileRequestDTO
ProjectRequestDTO
TechnologyRequestDTO
FeedbackRequestDTO
```

## Response DTOs

Utilizados para retornar informações ao cliente.

```text
ProfileResponseDTO
ProjectResponseDTO
TechnologyResponseDTO
FeedbackResponseDTO
```

---

# Validações

A API utiliza Bean Validation para validar os dados recebidos.

Entre as validações implementadas estão:

- campos obrigatórios;
- campos que não podem estar vazios;
- notas entre 1 e 5;
- URLs válidas;
- existência de perfil;
- existência de tecnologia;
- existência de projeto.

Exemplo de validação da nota:

```java
@NotNull(message = "A nota é obrigatória")
@Min(value = 1, message = "A nota deve ser no mínimo 1")
@Max(value = 5, message = "A nota deve ser no máximo 5")
Integer rating
```

---

# Endpoints

## Profiles

### Criar perfil

```http
POST /api/profiles
```

Exemplo:

```json
{
  "name": "Allan Isaac",
  "bio": "Desenvolvedor Back-End Java",
  "githubUrl": "https://github.com/allanisaac",
  "linkedinUrl": "https://www.linkedin.com/in/allanisaac-dev/"
}
```

Resposta esperada:

```text
201 Created
```

---

### Listar perfis

```http
GET /api/profiles
```

---

### Buscar perfil por ID

```http
GET /api/profiles/{id}
```

Exemplo:

```http
GET /api/profiles/1
```

---

### Atualizar perfil

```http
PUT /api/profiles/{id}
```

---

### Excluir perfil

```http
DELETE /api/profiles/{id}
```

Resposta:

```text
204 No Content
```

---

# Technologies

## Criar tecnologia

```http
POST /api/technologies
```

Exemplo:

```json
{
  "name": "Java"
}
```

Resposta:

```text
201 Created
```

---

## Listar tecnologias

```http
GET /api/technologies
```

---

## Buscar tecnologia por ID

```http
GET /api/technologies/{id}
```

---

## Atualizar tecnologia

```http
PUT /api/technologies/{id}
```

---

## Excluir tecnologia

```http
DELETE /api/technologies/{id}
```

Resposta:

```text
204 No Content
```

---

# Projects

## Criar projeto

```http
POST /api/projects
```

Exemplo:

```json
{
  "title": "DevShowcase API",
  "description": "API REST para compartilhamento de projetos de desenvolvedores.",
  "githubUrl": "https://github.com/Allan8606/devshowcase-UAPI",
  "projectUrl": "",
  "profileId": 1,
  "technologyIds": [
    1
  ]
}
```

Resposta:

```text
201 Created
```

---

## Listar projetos

```http
GET /api/projects
```

A listagem possui paginação e permite filtrar os projetos por tecnologia.

### Parâmetros

```text
technology
page
size
```

### Exemplo sem filtro

```http
GET /api/projects?page=0&size=10
```

### Exemplo com filtro

```http
GET /api/projects?technology=Java&page=0&size=10
```

### Paginação

A paginação utiliza índice iniciado em `0`.

Portanto:

```text
page=0 → primeira página
page=1 → segunda página
page=2 → terceira página
```

O parâmetro `size` determina quantos projetos serão retornados por página.

Exemplo:

```http
GET /api/projects?page=0&size=10
```

Retorna a primeira página com até 10 projetos.

---

## Buscar projeto por ID

```http
GET /api/projects/{id}
```

---

## Atualizar projeto

```http
PUT /api/projects/{id}
```

---

## Excluir projeto

```http
DELETE /api/projects/{id}
```

Resposta:

```text
204 No Content
```

---

# Feedbacks

As avaliações são cadastradas diretamente em um projeto.

## Cadastrar feedback

```http
POST /api/projects/{id}/feedbacks
```

Exemplo:

```http
POST /api/projects/1/feedbacks
```

Body:

```json
{
  "rating": 5,
  "comment": "Excelente projeto!"
}
```

A nota deve estar entre:

```text
1 e 5
```

Depois do cadastro, a API calcula novamente a média das avaliações do projeto e atualiza o campo:

```text
averageRating
```

---

## Listar feedbacks

```http
GET /api/feedbacks
```

---

## Buscar feedback por ID

```http
GET /api/feedbacks/{id}
```

---

## Atualizar feedback

```http
PUT /api/feedbacks/{id}
```

Exemplo:

```json
{
  "rating": 4,
  "comment": "Projeto muito bom."
}
```

Ao alterar uma avaliação, a média do projeto também é recalculada.

---

## Excluir feedback

```http
DELETE /api/feedbacks/{id}
```

---

# Upvotes

Cada projeto possui uma quantidade de curtidas/upvotes.

## Incrementar upvote

```http
PUT /api/projects/{id}/upvote
```

Exemplo:

```http
PUT /api/projects/1/upvote
```

A cada chamada desse endpoint, o número de upvotes do projeto é incrementado.

Exemplo:

```text
Antes:
upvotes = 3

Depois:
upvotes = 4
```

---

# Cálculo da média das avaliações

A média do projeto é calculada com base nas avaliações cadastradas.

Exemplo:

```text
Avaliação 1 → 5
Avaliação 2 → 4
Avaliação 3 → 3
```

Cálculo:

```text
(5 + 4 + 3) / 3 = 4.0
```

Resultado armazenado no projeto:

```text
averageRating = 4.0
```

Quando uma avaliação é criada, alterada ou removida, a aplicação pode recalcular a média para manter o valor atualizado.

---

# Tratamento global de exceções

A aplicação possui um manipulador global utilizando:

```java
@RestControllerAdvice
```

O tratamento está centralizado em:

```text
GlobalExceptionHandler
```

Isso evita que cada Controller precise tratar os erros individualmente.

---

# ResourceNotFoundException

Foi criada uma exceção personalizada:

```java
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

Ela é utilizada quando um recurso solicitado não existe.

Exemplos:

```text
Perfil não encontrado
Projeto não encontrado
Tecnologia não encontrada
Feedback não encontrado
```

---

# Erros de validação

Quando uma requisição possui dados inválidos, a aplicação retorna:

```text
400 Bad Request
```

Exemplo de validação:

```json
{
  "rating": 10,
  "comment": ""
}
```

A API pode retornar mensagens como:

```text
A nota deve ser no máximo 5, O comentario é obrigatório
```

---

# Erros de recurso não encontrado

Quando o ID informado não corresponde a nenhum recurso existente:

```text
404 Not Found
```

Exemplo:

```http
PUT /api/projects/999/upvote
```

Resposta:

```text
Projeto não encontrado
```

---

# Swagger / OpenAPI

A API possui documentação interativa utilizando Swagger UI.

## Ambiente local

```text
http://localhost:8080/swagger-ui/index.html
```

## Ambiente de produção

```text
https://devshowcase-uapi-oficial.onrender.com/swagger-ui/index.html
```

O Swagger permite:

- visualizar os endpoints;
- visualizar parâmetros;
- visualizar modelos;
- enviar requisições;
- testar os endpoints;
- visualizar respostas HTTP.

---

# OpenAPI

A especificação OpenAPI também pode ser acessada através de:

```text
/v3/api-docs
```

Exemplo em produção:

```text
https://devshowcase-uapi-oficial.onrender.com/v3/api-docs
```

---

# Configuração do banco de dados

A aplicação utiliza PostgreSQL.

No ambiente de produção, os dados de conexão não ficam diretamente no código.

As informações são obtidas através de variáveis de ambiente.

Exemplo do arquivo:

```yaml
spring:
  application:
    name: devshowcase

  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}

  jpa:
    hibernate:
      ddl-auto: update

server:
  port: ${PORT:8080}
```

---

# Variáveis de ambiente

As seguintes variáveis são utilizadas:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

Também existe suporte para a variável:

```text
PORT
```

No Render, o valor da porta é fornecido pelo próprio ambiente.

---

# PostgreSQL em produção

O banco de dados PostgreSQL foi provisionado utilizando o ambiente do Render.

A aplicação utiliza uma URL JDBC no seguinte formato:

```text
jdbc:postgresql://HOST:PORT/DATABASE
```

Exemplo de estrutura:

```text
jdbc:postgresql://host-do-banco:5432/nome-do-banco
```

As credenciais são configuradas separadamente através das variáveis:

```text
DB_USERNAME
DB_PASSWORD
```

---

# Deploy

O deploy da aplicação foi realizado utilizando o Render.

A arquitetura de produção é:

```text
GitHub
   ↓
Render Web Service
   ↓
Docker
   ↓
Spring Boot
   ↓
Render PostgreSQL
```

---

# Deploy contínuo

O projeto está conectado ao GitHub.

Quando alterações são enviadas para a branch:

```text
main
```

o Render pode realizar automaticamente um novo deploy.

Fluxo utilizado:

```text
Alteração no código
        ↓
git add
        ↓
git commit
        ↓
git push
        ↓
GitHub
        ↓
Render detecta a alteração
        ↓
Novo build
        ↓
Novo deploy
```

---

# Docker

A aplicação possui um `Dockerfile` para gerar a imagem da aplicação.

```dockerfile
FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/devshowcase-0.0.1-SNAPSHOT.jar"]
```

---

# Executando com Docker

Para gerar a imagem:

```bash
docker build -t devshowcase .
```

Depois de gerar a imagem, o projeto pode ser executado utilizando Docker.

---

# Executando localmente

## Pré-requisitos

É necessário possuir instalado:

```text
Java 17
Git
PostgreSQL
Maven
Docker
```

O projeto possui Maven Wrapper, portanto também é possível utilizar:

```bash
./mvnw
```

No Windows:

```bash
mvnw.cmd
```

---

# Clonando o projeto

```bash
git clone https://github.com/Allan8606/devshowcase-UAPI.git
```

Entre no diretório do projeto:

```bash
cd devshowcase-UAPI
```

---

# Configurando o banco local

É necessário possuir um PostgreSQL configurado.

A aplicação deve apontar para o banco local através da configuração do Spring Boot.

Exemplo:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/devshowcase
    username: postgres
    password: SUA_SENHA
```

As credenciais locais devem ser configuradas de acordo com o ambiente de desenvolvimento.

---

# Executando a aplicação

Utilizando Maven Wrapper:

```bash
./mvnw spring-boot:run
```

No Windows:

```bash
mvnw.cmd spring-boot:run
```

Outra opção é gerar o projeto:

```bash
./mvnw clean package
```

e executar o `.jar` gerado.

---

# Acessando a API localmente

Depois de iniciar a aplicação:

```text
http://localhost:8080
```

Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI:

```text
http://localhost:8080/v3/api-docs
```

---

# Testes dos endpoints

Os endpoints foram testados utilizando a documentação interativa do Swagger.

Entre os testes realizados estão:

### Feedback

```http
POST /api/projects/{id}/feedbacks
```

Validação da nota e comentário.

### Upvote

```http
PUT /api/projects/{id}/upvote
```

Verificação do incremento das curtidas.

### Paginação

```http
GET /api/projects?page=0&size=10
```

Verificação da divisão dos resultados em páginas.

### Filtro por tecnologia

```http
GET /api/projects?technology=Java&page=0&size=10
```

Verificação do retorno de projetos associados à tecnologia informada.

### Tratamento de erros

Teste de recurso inexistente:

```http
PUT /api/projects/999/upvote
```

Resultado:

```text
404 Not Found
```

Teste de validação:

```text
400 Bad Request
```

---

# Exemplo de resposta paginada

Uma consulta como:

```http
GET /api/projects?page=0&size=10
```

retorna uma estrutura paginada fornecida pelo Spring Data.

Exemplo simplificado:

```json
{
  "content": [
    {
      "id": 1,
      "title": "DevShowcase API",
      "description": "API REST para compartilhamento de projetos.",
      "githubUrl": "https://github.com/Allan8606/devshowcase-UAPI",
      "projectUrl": "",
      "profileId": 1,
      "technologyIds": [
        1
      ],
      "averageRating": 5.0,
      "upvotes": 3
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 1
}
```

---

# Exemplo de resposta de projeto

```json
{
  "id": 1,
  "title": "DevShowcase API",
  "description": "API REST para compartilhamento de projetos de desenvolvedores.",
  "githubUrl": "https://github.com/Allan8606/devshowcase-UAPI",
  "projectUrl": "",
  "profileId": 1,
  "technologyIds": [
    1
  ],
  "averageRating": 5.0,
  "upvotes": 2
}
```

---

# Exemplo de feedback

### Requisição

```json
{
  "rating": 5,
  "comment": "Excelente projeto!"
}
```

### Resultado

```json
{
  "id": 1,
  "rating": 5,
  "comment": "Excelente projeto!",
  "projectId": 1
}
```

---

# Regras de negócio implementadas

## Avaliação

A nota de um feedback deve estar entre:

```text
1 e 5
```

O projeto possui um campo:

```text
averageRating
```

que representa a média das avaliações.

---

## Upvote

Cada projeto inicia com:

```text
upvotes = 0
```

Quando o endpoint de upvote é chamado:

```http
PUT /api/projects/{id}/upvote
```

o valor é incrementado.

---

## Filtro por tecnologia

É possível buscar projetos utilizando o nome da tecnologia:

```http
GET /api/projects?technology=Java
```

O filtro não diferencia letras maiúsculas e minúsculas.

Exemplos:

```text
Java
java
JAVA
```

podem corresponder à mesma tecnologia.

---

## Paginação

A listagem de projetos utiliza `Page` do Spring Data.

Exemplo:

```http
GET /api/projects?page=0&size=10
```

---

# Banco de dados

As entidades são persistidas utilizando JPA/Hibernate.

O Hibernate cria e atualiza as estruturas do banco de acordo com as entidades configuradas.

Em produção:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
```

---

# Principais conceitos utilizados

Durante o desenvolvimento foram aplicados diversos conceitos importantes de Back-End:

- API REST;
- HTTP;
- CRUD;
- Spring Boot;
- Spring Data JPA;
- Hibernate;
- ORM;
- PostgreSQL;
- Entidades;
- DTOs;
- Records;
- Repositories;
- Services;
- Controllers;
- Bean Validation;
- Exceptions;
- Tratamento global de exceções;
- Relacionamentos 1:N;
- Relacionamentos N:N;
- Paginação;
- Filtros;
- Swagger;
- OpenAPI;
- Docker;
- Variáveis de ambiente;
- Git;
- GitHub;
- Deploy em nuvem.

---

# Status do projeto

A etapa do projeto foi concluída com as funcionalidades previstas.

```text
[✓] Configuração do projeto
[✓] Repositório GitHub
[✓] Modelagem das entidades
[✓] Relacionamento Profile → Project
[✓] Relacionamento Project → Technology
[✓] Relacionamento Project → Feedback
[✓] Repositories
[✓] DTOs
[✓] Validações
[✓] CRUD de Profiles
[✓] CRUD de Technologies
[✓] CRUD de Projects
[✓] Cadastro de Feedback
[✓] Cálculo da média das avaliações
[✓] Edição de Feedback
[✓] Exclusão de Feedback
[✓] Sistema de Upvote
[✓] Filtro por tecnologia
[✓] Paginação
[✓] Tratamento global de exceções
[✓] Swagger/OpenAPI
[✓] Docker
[✓] PostgreSQL em nuvem
[✓] Variáveis de ambiente
[✓] Deploy no Render
[✓] Deploy contínuo através do GitHub
[✓] Testes em produção
```

---

# API em produção

A aplicação está disponível em:

```text
https://devshowcase-uapi-oficial.onrender.com
```

Swagger:

```text
https://devshowcase-uapi-oficial.onrender.com/swagger-ui/index.html
```

OpenAPI:

```text
https://devshowcase-uapi-oficial.onrender.com/v3/api-docs
```

---

# Repositório

GitHub:

```text
https://github.com/Allan8606/devshowcase-UAPI
```

---

# Autor

**Allan Isaac**

Desenvolvedor Back-End em formação, com foco em Java e Spring Boot.

LinkedIn:

```text
https://www.linkedin.com/in/allanisaac-dev/
```

GitHub:

```text
https://github.com/Allan8606
```

---

# Considerações finais

O DevShowcase API foi desenvolvido com o objetivo de praticar conceitos fundamentais de desenvolvimento Back-End utilizando Java e Spring Boot.

Ao longo do projeto foram trabalhados desde a modelagem do banco de dados e criação das entidades até a implementação das regras de negócio, tratamento de erros, documentação da API e publicação em ambiente de produção.

O projeto também demonstra a utilização de ferramentas e práticas comuns no desenvolvimento profissional, como:

```text
Git
GitHub
Spring Boot
Spring Data JPA
PostgreSQL
Docker
Swagger/OpenAPI
Render
Variáveis de ambiente
```

O resultado é uma API REST funcional, documentada e disponível em ambiente de produção.
