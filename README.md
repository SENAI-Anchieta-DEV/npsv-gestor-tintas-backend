# 🎨 Gestor Tintas — Backend API

API REST do **Gestor Tintas**, sistema de gestão para lojas de tintas com integração IoT (balança ESP32 via MQTT). Desenvolvida em **Java 21 + Spring Boot**, responsável por autenticação, controle de estoque, produção, vendas, pedidos e processamento de pesagens em tempo real.

> Parte do projeto **NPSV — Gestor Tintas**. Repositórios relacionados: FrontEnd, Mobile e IoT (firmware ESP32).

---

## 🧰 Tech Stack

| Camada | Tecnologia |
| :--- | :--- |
| Linguagem | Java 21 |
| Framework | Spring Boot 4.0.2 (Web MVC, Data JPA, Security, Validation, Actuator) |
| Segurança | Spring Security + JWT (jjwt 0.11.5) |
| Banco (dev) | H2 em memória |
| Banco (prod) | PostgreSQL |
| Migrations | Flyway 12.3.0 |
| Mensageria | Spring Integration MQTT + Eclipse Paho 1.2.5 |
| Documentação | Springdoc OpenAPI UI (Swagger) |
| Build | Maven (wrapper incluído) |
| Container | Docker (Eclipse Temurin 21) |
| Deploy | Render (Docker runtime) |

---

## 📚 Documentação da API (Swagger)

O projeto possui documentação interativa da API gerada automaticamente via **OpenAPI/Swagger**. Com a aplicação em execução, você pode visualizar os endpoints, os *schemas* dos DTOs e testar as requisições diretamente pelo navegador:

- **Swagger UI:** [https://gestor-tintas-backend-api.onrender.com/swagger-ui/index.html](https://gestor-tintas-backend-api.onrender.com/swagger-ui/index.html)
- **OpenAPI JSON:** [https://gestor-tintas-backend-api.onrender.com/v3/api-docs](https://gestor-tintas-backend-api.onrender.com/v3/api-docs)

---

## 📋 Pré-requisitos

Para rodar **localmente sem Docker**:

- **JDK 21** (Eclipse Temurin recomendado)
- **Maven** — não é obrigatório instalar, o projeto inclui o wrapper (`./mvnw`)
- **Git**
- *(Opcional)* Um **broker MQTT** local (ex.: Mosquitto em `tcp://localhost:1883`) caso queira testar a integração IoT. Sem o broker, a aplicação sobe normalmente, apenas sem consumir mensagens de pesagem.

Para rodar **com Docker**:

- **Docker** 20+

Para o **perfil de produção**:

- Instância **PostgreSQL** acessível

---

## ⚙️ Variáveis de Ambiente

A aplicação usa dois perfis: **`dev`** (padrão, H2 em memória) e **`prod`** (PostgreSQL). O perfil é controlado por `SPRING_PROFILES_ACTIVE`.

### Comuns a todos os perfis

| Variável | Descrição | Default | Obrigatória |
| :--- | :--- | :--- | :---: |
| `SPRING_PROFILES_ACTIVE` | Perfil ativo (`dev` ou `prod`) | `dev` | Não |
| `PORT` | Porta HTTP da aplicação | `8080` | Não |
| `SECURITY_JWT_SECRET` | Segredo de assinatura do JWT (HS256, mín. 32 chars) | chave de dev embutida | **Sim em prod** |
| `SECURITY_JWT_ACCESS_EXPIRATION` | Expiração do token de acesso (segundos) | `3600` | Não |
| `SECURITY_JWT_REFRESH_EXPIRATION` | Expiração do refresh (segundos) | `604800` | Não |
| `APP_CORS_ALLOWED_ORIGINS` | Origens permitidas no CORS | `http://localhost:5173` | Não |
| `SISTEMA_ADMIN_EMAIL` | E-mail do admin provisório criado no boot | *(vazio)* | Não |
| `SISTEMA_ADMIN_SENHA` | Senha do admin provisório | *(vazio)* | Não |

> Se `SISTEMA_ADMIN_EMAIL` e `SISTEMA_ADMIN_SENHA` forem preenchidas, um usuário **ADMIN** é criado automaticamente na inicialização (útil para o primeiro acesso).

### Somente perfil `prod` (PostgreSQL)

| Variável | Descrição | Obrigatória |
| :--- | :--- | :---: |
| `DATABASE_HOST` | Host do PostgreSQL | **Sim** |
| `DATABASE_PORT` | Porta do PostgreSQL | **Sim** |
| `DATABASE_NAME` | Nome do banco | **Sim** |
| `DATABASE_USER` | Usuário do banco | **Sim** |
| `DATABASE_PASSWORD` | Senha do banco | **Sim** |

### Integração MQTT (opcional)

| Variável | Descrição | Default |
| :--- | :--- | :--- |
| `mqtt.broker-url` | URL do broker | `tcp://localhost:1883` |
| `mqtt.client-id` | ID do cliente MQTT | `app-local` |
| `mqtt.username` | Usuário do broker | *(vazio)* |
| `mqtt.password` | Senha do broker | *(vazio)* |

> Estas podem ser sobrescritas via variáveis de ambiente padrão do Spring (ex.: `MQTT_BROKER_URL`) ou em `application.properties`.

### Exemplo `.env` (desenvolvimento)

```env
SPRING_PROFILES_ACTIVE=dev
SECURITY_JWT_SECRET=mysupersecretkeymysupersecretkey123!
SISTEMA_ADMIN_EMAIL=admin@gestortintas.com
SISTEMA_ADMIN_SENHA=admin123
APP_CORS_ALLOWED_ORIGINS=http://localhost:5173
```

> ⚠️ **Nunca** commite o `.env` real. O `.gitignore` já ignora `.env` e variantes.

---

## 🚀 Passos de Execução

### Opção A — Local com Maven Wrapper (perfil dev / H2)

```bash
# 1. Clone o repositório
git clone <url-do-repositorio>
cd npsv-gestor-tintas-backend

# 2. (Linux/macOS) dê permissão de execução ao wrapper
chmod +x mvnw

# 3. Rode a aplicação (perfil dev é o padrão)
./mvnw spring-boot:run
```

No Windows use `mvnw.cmd spring-boot:run`.

A API sobe em **http://localhost:8080**.

### Opção B — Docker

```bash
# 1. Build da imagem
docker build -t gestor-tintas-backend .

# 2. Run (perfil dev)
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=dev \
  -e SISTEMA_ADMIN_EMAIL=admin@gestortintas.com \
  -e SISTEMA_ADMIN_SENHA=admin123 \
  gestor-tintas-backend
```

### Build do `.jar` para produção

```bash
./mvnw clean package -DskipTests
java -jar target/*.jar
```

---

## ✅ Verificação / Health Check

Com a aplicação no ar:

```bash
# Liveness / Readiness (Actuator)
curl http://localhost:8080/actuator/health
curl http://localhost:8080/actuator/health/readiness
```

**Console H2** (somente perfil `dev`): http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:db`
- Usuário: `sa` — Senha: *(em branco)*

> ⚠️ O H2 é **em memória**: os dados são perdidos a cada reinício da aplicação.

---

## 🔐 Autenticação

A maioria dos endpoints exige **JWT**. Fluxo básico:

```bash
# 1. Login (use as credenciais do admin provisório)
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@gestortintas.com","senha":"admin123"}'

# 2. Use o token retornado no header das demais requisições
curl http://localhost:8080/api/produtos \
  -H "Authorization: Bearer <TOKEN>"
```

Rotas públicas: `/auth/**`, `/actuator/**`, `/swagger-ui/**`, `/v3/api-docs/**`, `/error`.

Perfis de acesso (roles): `ADMIN`, `COLORISTA`, `VENDEDOR`.

---

## 🗄️ Banco de Dados & Migrations

- As migrations Flyway ficam em `src/main/resources/db/migration` e rodam **automaticamente** na inicialização.
- `spring.jpa.hibernate.ddl-auto=validate` — o Hibernate apenas valida o schema; toda mudança estrutural deve ser feita via nova migration (`V{N}__descricao.sql`).

---

## 📡 Integração IoT (MQTT)

O backend assina os tópicos do ESP32:

- `v1/dispositivos/+/pesagem` — eventos de pesagem da balança
- `v1/dispositivos/+/status` — heartbeat / LWT do dispositivo

e publica comandos em `v1/dispositivos/{id}/comando`. A validação de tolerância (**RN01 — margem de 5%**) é aplicada no backend apenas quando o firmware envia `estavel=true`. Sem um broker ativo, a aplicação sobe normalmente — apenas não processa pesagens.

---

## 📂 Estrutura do Projeto
src/main/java/com/senai/npsv_gestor_tintas_backend/

├── application/      # DTOs e Services (regras de negócio)

├── domain/           # Entities, Enums, Repositories, Exceptions

├── infrastructure/   # Config (MQTT, Security), JWT, Subscribers MQTT

└── interface_ui/     # Controllers REST e tratamento global de exceções

src/main/resources/

├── application.properties        # Config base

├── application-dev.properties    # Perfil dev (H2)

├── application-prod.properties   # Perfil prod (PostgreSQL)

└── db/migration/                 # Migrations Flyway
