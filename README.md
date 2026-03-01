# 📋 Project Attorney Users - API REST

Este projeto é uma API REST desenvolvida com Spring Boot para gerenciamento de usuários, incluindo autenticação segura com JWT e recuperação de senha via e-mail.

---

## 🚀 Tecnologias Utilizadas

- ☕ **Java 17**
- 🌱 **Spring Boot 3.5**
- 📦 **Maven**
- 🗃️ **Spring Data JPA**
- 🐘 **PostgreSQL** (Banco de dados principal)
- 💾 **H2 Database** (Banco de dados em memória para testes)
- 🔒 **Spring Security & JWT** (Autenticação e Autorização)
- 📧 **Spring Boot Starter Mail** (Envio de e-mails)
- ✍️ **Lombok** (Redução de código boilerplate)
- ✅ **Bean Validation** (Validação de dados)

---

## ✨ Funcionalidades

- **Cadastro de Usuários**: Criação de novos usuários com validação de dados.
- **Autenticação (Login)**: Geração de token JWT para acesso seguro.
- **Gerenciamento de Sessão**: Armazenamento de tokens de acesso no banco de dados.
- **Recuperação de Senha**:
    - Solicitação de código de recuperação via e-mail.
    - Validação do código e redefinição de senha.
- **CRUD de Usuários**: Listagem, atualização e remoção de usuários.

---

## 📂 Estrutura do Projeto

O projeto segue uma arquitetura em camadas:

- `controller`: Endpoints da API.
- `business`: Regras de negócio (Service e DTOs).
- `infrastructure`:
    - `entitys`: Entidades JPA (`Usuario`, `Token`, `PasswordResetToken`).
    - `repository`: Interfaces de acesso ao banco de dados.
    - `security`: Configurações de segurança e utilitários JWT.

---

## ⚙️ Configuração

Para executar o projeto, é necessário configurar as variáveis de ambiente no arquivo `application.properties` ou no ambiente de execução:

```properties
spring.datasource.url=${URL_BASE}
spring.datasource.username=${USER_NAME}
spring.datasource.password=${PASSWORD}
spring.mail.username=${USER_NAME_EMAIL}
spring.mail.password=${PASSWORD_EMAIL}
```

---

## ▶️ Como executar o projeto

1. **Clone o repositório:**

   ```bash
   git clone https://github.com/vinnixp098/project-attorney-users.git
   cd project-attorney-users
   ```

2. **Compile e execute:**

   ```bash
   ./mvnw spring-boot:run
   ```

---

## 🔗 Endpoints Principais

- `POST /api/usuarios`: Cadastrar usuário.
- `POST /api/usuarios/login`: Autenticar e receber token JWT.
- `POST /api/usuarios/recuperar-senha`: Solicitar código de recuperação.
- `POST /api/usuarios/validar-codigo`: Redefinir senha com o código recebido.
