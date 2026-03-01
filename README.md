# 📋 Cadastro de Usuários - API REST

Este projeto tem como finalidade a criação de uma API REST simples para gerenciamento de usuários.  
A API permite **criar**, **listar**, **atualizar** e **deletar** registros de usuários.

---

## 🚀 Tecnologias Utilizadas

- ☕ **Java 24**
- 🌱 **Spring Boot 3.5**
- 📦 **Maven**
- 🗃️ **Spring Data JPA**
- 💾 **H2 Database (em memória)**
- 🔧 **Spring Web**
- ✍️ **Lombok**

---

## 📂 Estrutura do Projeto

O projeto segue uma arquitetura simples com os pacotes organizados em:

- `controller`: Responsável por expor os endpoints da API.
- `model`: Entidade `Usuario`.
- `repository`: Interface JPA para comunicação com o banco.
- `service` (opcional): Regras de negócio (se aplicável).

---

## 💻 Acesso ao Console do H2

A aplicação utiliza o **H2 Database** em memória. Para acessar a interface web do banco e visualizar os dados:

🔗 Acesse:  
[http://localhost:8080/h2-console](http://localhost:8080/h2-console)

🛠️ Configure a conexão com os seguintes dados:

- **JDBC URL**: `jdbc:h2:mem:usuario`
- **User Name**: `sa`
- **Password**: *(em branco)*

---

## ▶️ Como executar o projeto

1. **Clone o repositório:**

   ```bash
   git clone https://github.com/vinnixp098/cadastro-usuario.git
   cd cadastro-usuario
