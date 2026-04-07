# 🚀 User Management API

API REST desenvolvida com Spring Boot para autenticação e autorização de usuários utilizando **JWT (JSON Web Token)** e controle de acesso baseado em **roles (USER / ADMIN)**.

---

# 📌 📖 Sobre o projeto

Este projeto tem como objetivo implementar um sistema completo de autenticação com:

* 🔐 Login com JWT
* 👤 Cadastro de usuários
* 🔒 Controle de acesso por roles
* 🛡️ Proteção de endpoints
* 📚 Documentação com Swagger (OpenAPI 3)

---

# ⚙️ 🛠️ Tecnologias utilizadas

* Java 17+
* Spring Boot
* Spring Security
* JWT (Auth0)
* Maven
* Swagger (OpenAPI 3 - Springdoc)

---

# 🚀 ▶️ Como executar o projeto

### 1. Clonar o repositório

```bash
git clone https://github.com/alitakallyne/user-management-api.git
```

### 2. Acessar a pasta

```bash
cd usermanagement
```

### 3. Rodar o projeto

```bash
mvn spring-boot:run
```

---

# 🌐 📚 Documentação da API (Swagger)

Acesse:

```text
http://localhost:8080/api/swagger-ui/index.html
```

---

# 🔐 🔑 Autenticação

A API utiliza JWT para autenticação.

## Como usar:

1. Faça login no endpoint `/auth/login`
2. Copie o token retornado
3. No Swagger ou requisições HTTP, utilize:

```text
Authorization: Bearer SEU_TOKEN
```

---

# 👤 📌 Funcionalidades

## 🔹 Registro de usuário

```http
POST /auth/register
```

### Exemplo:

```json
{
  "name": "João Silva",
  "email": "joao@gmail.com",
  "password": "123456"
}
```

---

## 🔹 Login

```http
POST /auth/login
```

### Exemplo:

```json
{
  "email": "joao@gmail.com",
  "password": "123456"
}
```

### Resposta:

```json
{
  "token": "jwt_token_aqui"
}
```

---

## 👤 🔹 Dashboard do usuário

```http
GET /user/dashboard
```

* 🔓 Acesso: USER ou ADMIN

---

## 👑 🔹 Painel Admin

```http
GET /admin/panel
```

* 🔒 Acesso: somente ADMIN

---

# 🔐 🔒 Controle de acesso

| Endpoint  | Acesso      |
| --------- | ----------- |
| /auth/**  | Público     |
| /user/**  | USER, ADMIN |
| /admin/** | ADMIN       |

---

# 🧪 🧾 Testes com arquivo .http

O projeto possui arquivos `.http` para testes utilizando REST Client (VS Code).

📁 Localização sugerida:

```
/requests/auth.http
```

### Exemplo de uso:

```http
### Login
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "email": "joao@gmail.com",
  "password": "123456"
}
```

---

# 🔐 🧠 Roles

As roles são definidas como:

* ROLE_USER → acesso padrão
* ROLE_ADMIN → acesso administrativo

### ⚠️ Importante

* Usuários são criados como `ROLE_USER` por padrão
* A role ADMIN é utilizada apenas para testes em ambiente de desenvolvimento

---

# 🛡️ 🔑 Segurança

* Senhas criptografadas com BCrypt
* Autenticação via JWT
* Filtro de validação de token
* Proteção de rotas com Spring Security

---

# 📦 📂 Estrutura do projeto

```text
└───com
    └───alita
        └───usermanagement
            │   UsermanagementApplication.java
            │   
            ├───config
            │       AuthConfig.java
            │       JWTUserData.java
            │       OpenApiConfig.java
            │       SecurityConfig.java
            │       SecurityFilter.java
            │       TokenConfig.java
            │
            ├───controller
            │       AdminController.java
            │       AuthController.java
            │       UserController.java
            │
            ├───infrastructure
            │   ├───dto
            │   │   ├───request
            │   │   │       LoginRequest.java
            │   │   │       RegisterUserRequest.java
            │   │   │
            │   │   └───response
            │   │           LoginResponse.java
            │   │           RegisterUserResponse.java
            │   │
            │   ├───entity
            │   │       Role.java
            │   │       User.java
            │   │
            │   └───repository
            │           UserRepository.java
            │
            └───requests
                    auth.http

```

---

# 💡 📈 Melhorias futuras

* Refresh Token
* Controle de permissões mais granular
* Cadastro de ADMIN protegido
* Testes automatizados (JUnit)
* Deploy em nuvem

---

# 👩‍💻 Autor

Desenvolvido por **Alita Kallyne do Nascimento**

---

# ⭐ Considerações finais

Este projeto foi desenvolvido com foco em aprendizado e boas práticas de autenticação e segurança em APIs REST com Spring Boot.

---
