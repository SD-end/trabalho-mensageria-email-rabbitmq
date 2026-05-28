# Sistema de Envio de E-mails em Lote com Java e RabbitMQ

## Sobre o projeto

Sistema backend desenvolvido em Java com Spring Boot para envio assíncrono de e-mails em lote utilizando RabbitMQ.

O sistema permite:

* Cadastrar destinatários;
* Listar destinatários;
* Criar envios de e-mail;
* Solicitar envio em lote;
* Processar mensagens de forma assíncrona via RabbitMQ.

O objetivo principal é demonstrar conceitos de mensageria, separando a requisição inicial do processamento do envio.

---

# Tecnologias utilizadas

* Java
* Spring Boot
* Spring Web
* Spring Data JPA
* Bean Validation
* RabbitMQ
* PostgreSQL
* Docker / Docker Compose
* Maven
* Postman

---

# Arquitetura de mensageria

O sistema utiliza RabbitMQ com:

* **Exchange:** `email.exchange`
* **Fila:** `fila.email`
* **Routing Key:** `email.enviar`

Fluxo principal:

```text
POST /email/lote
↓
Mensagens publicadas no RabbitMQ
↓
Consumer processa a fila
↓
Status atualizado no PostgreSQL
```

---

# Funcionalidades

* Cadastro de destinatários
* Listagem de destinatários
* Envio individual de e-mails
* Envio em lote
* Publicação de mensagens no RabbitMQ
* Consumer com `@RabbitListener`
* Atualização de status (`PENDENTE` → `ENVIADO`)
* Persistência no PostgreSQL

---

# Como executar o projeto

## 1. Subir os containers

```bash
docker compose -p email-rabbit up -d
```

---

## 2. Acessar o RabbitMQ

```text
http://localhost:15673
```

Login:

```text
Usuário: guest
Senha: guest
```

---

## 3. Executar o backend

Executar a classe:

```text
EmailRabbitmqApplication.java
```

Aplicação disponível em:

```text
http://localhost:8080
```

---

# Configuração do application.properties

```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/email_db
spring.datasource.username=email_user
spring.datasource.password=email_pass

spring.rabbitmq.host=localhost
spring.rabbitmq.port=5673
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

---

# Endpoints principais

## Destinatários

### Cadastrar

```http
POST /destinatarios
```

### Listar

```http
GET /destinatarios
```

---

## E-mails

### Envio individual

```http
POST /email
```

### Envio em lote

```http
POST /email/lote
```

### Listar envios

```http
GET /email
```

### Buscar envio por ID

```http
GET /email/{id}
```

---

# Exemplo de envio em lote

```json
{
  "assunto": "Comunicado",
  "mensagem": "Nova campanha disponível."
}
```

O sistema:

1. Busca os destinatários;
2. Cria registros com status `PENDENTE`;
3. Publica mensagens no RabbitMQ;
4. O consumer processa os envios;
5. Atualiza o status para `ENVIADO`.

---

# Estrutura principal do projeto

```text
config/
controller/
dto/
model/
repository/
service/
```

Principais classes:

* `RabbitMQConfig`
* `EmailController`
* `EmailService`
* `EmailConsumer`
* `DestinatarioService`

---

# Banco de dados

Tabelas principais:

* `destinatarios`
* `email`

Status utilizados:

* `PENDENTE`
* `ENVIADO`

---

# Comandos úteis

Subir containers:

```bash
docker compose -p email-rabbit up -d
```

Parar containers:

```bash
docker compose -p email-rabbit down
```

Acessar PostgreSQL:

```bash
docker exec -it postgres-email psql -U email_user -d email_db
```

---

# Observações

Nesta versão acadêmica, o envio de e-mails é simulado no console pelo consumer.

O foco do projeto é demonstrar:

* Producer
* Consumer
* Exchange
* Queue
* Routing Key
* Processamento assíncrono com RabbitMQ

Recursos como DLQ, retry e integração com serviços reais de e-mail podem ser adicionados futuramente.

---

# Integrantes

* Gabriel Kaynan
* João Gabriel Borges
* João Victor Fernandes
* Sebastian Domingues
