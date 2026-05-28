# Sistema de Envio de E-mails em Lote com Java, RabbitMQ e React

Este projeto foi desenvolvido como atividade acadêmica de mensageria, com base nos conceitos apresentados na documentação de arquitetura com RabbitMQ.

A proposta é demonstrar um sistema capaz de cadastrar destinatários, criar mensagens e realizar envio em lote de forma assíncrona utilizando RabbitMQ.

O projeto possui:

- Backend em Java com Spring Boot;
- Frontend em React com TypeScript;
- Banco de dados PostgreSQL;
- Mensageria com RabbitMQ;
- Docker Compose para subir os serviços externos.

## Objetivo

O objetivo do sistema é demonstrar, de forma prática, o uso de mensageria com RabbitMQ em uma aplicação Java.

O fluxo principal é:

```text
Frontend React
      ↓
Backend Spring Boot
      ↓
Banco PostgreSQL
      ↓
Producer publica mensagem no RabbitMQ
      ↓
Exchange email.exchange
      ↓
Routing key email.enviar
      ↓
Fila fila.email
      ↓
Consumer processa a mensagem
      ↓
Status atualizado no banco
```

## Relação com a documentação

A documentação utilizada como base apresenta uma arquitetura de mensageria confiável com RabbitMQ, abordando conceitos como:

- Producer;
- Consumer;
- Exchange;
- Queue;
- Binding;
- Routing Key;
- Comunicação assíncrona;
- Processamento desacoplado;
- Confiabilidade no processamento de mensagens.

Nesta entrega, foi implementada uma versão essencial e funcional desses conceitos, focada no serviço de envio de e-mails em lote.

Recursos mais avançados, como retry, DLQ, idempotência e múltiplos microsserviços, foram considerados como possíveis evoluções futuras.

## Funcionalidades

O sistema permite:

- Cadastrar destinatários;
- Listar destinatários cadastrados;
- Criar uma mensagem;
- Solicitar envio em lote;
- Publicar mensagens no RabbitMQ;
- Consumir mensagens da fila;
- Simular o envio de e-mails;
- Atualizar o status do envio no banco;
- Visualizar os envios pelo frontend;
- Consultar evidências no PostgreSQL, RabbitMQ e logs do backend.

## Tecnologias utilizadas

## Backend

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Bean Validation
- RabbitMQ
- PostgreSQL
- Maven

## Frontend

- React
- TypeScript
- Vite
- CSS
- Fetch API

## Infraestrutura

- Docker
- Docker Compose
- Postman
- RabbitMQ Management

## Estrutura do projeto

```text
Trabalho-mensageria
├── docker-compose.yml
├── README.md
├── email-rabbitmq
│   ├── README.md
│   ├── pom.xml
│   └── src
└── frontend-email
    ├── README.md
    ├── package.json
    └── src
```

## Serviços Docker

O projeto utiliza Docker Compose para executar:

- PostgreSQL;
- RabbitMQ com painel de gerenciamento.

## Como executar o projeto

## 1. Subir PostgreSQL e RabbitMQ

Na raiz do projeto, execute:

```bash
docker compose -p email-rabbit up -d
```

Isso irá subir os containers do PostgreSQL e do RabbitMQ.

## 2. Acessar o RabbitMQ

Acesse no navegador:

```text
http://localhost:15673
```

Credenciais:

```text
Usuário: guest
Senha: guest
```

## 3. Executar o backend

Entre na pasta do backend:

```bash
cd email-rabbitmq
```

Execute a aplicação pela IDE ou pela classe principal:

```text
EmailRabbitmqApplication.java
```

O backend ficará disponível em:

```text
http://localhost:8080
```

## 4. Executar o frontend

Em outro terminal, entre na pasta do frontend:

```bash
cd frontend-email
```

Instale as dependências:

```bash
npm install
```

Execute o frontend:

```bash
npm run dev
```

O frontend ficará disponível em:

```text
http://localhost:5173
```

## Configuração do RabbitMQ

A aplicação utiliza a seguinte configuração:

```text
Exchange: email.exchange
Tipo: direct

Fila: fila.email

Routing key: email.enviar
```

Funcionamento:

1. O backend cria um registro de envio no banco com status `PENDENTE`;
2. O producer publica o ID do envio na exchange `email.exchange`;
3. A exchange encaminha a mensagem para a fila `fila.email` usando a routing key `email.enviar`;
4. O consumer escuta a fila;
5. O consumer busca o envio no banco;
6. O envio é simulado no console;
7. O status é atualizado para `ENVIADO`.

## Endpoints principais

## Destinatários

Cadastrar destinatário:

```http
POST http://localhost:8080/destinatarios
```

Exemplo:

```json
{
  "nome": "João",
  "email": "joao@email.com"
}
```

Listar destinatários:

```http
GET http://localhost:8080/destinatarios
```

## E-mails

Enviar e-mail individual:

```http
POST http://localhost:8080/email
```

Exemplo:

```json
{
  "destinatario": "teste@email.com",
  "assunto": "Teste RabbitMQ",
  "mensagem": "Mensagem enviada pelo Postman"
}
```

Enviar e-mails em lote:

```http
POST http://localhost:8080/email/lote
```

Exemplo:

```json
{
  "assunto": "Comunicado em Lote",
  "mensagem": "Olá, temos uma nova mensagem para todos os destinatários cadastrados."
}
```

Listar envios:

```http
GET http://localhost:8080/email
```

Buscar envio por ID:

```http
GET http://localhost:8080/email/{id}
```

## Banco de dados

O banco utilizado é o PostgreSQL.

Para acessar o banco pelo terminal:

```bash
docker exec -it postgres-email psql -U email_user -d email_db
```

Listar tabelas:

```sql
\dt
```

Tabelas principais:

```text
destinatarios
email
```

Consultar destinatários:

```sql
select * from destinatarios;
```

Consultar envios:

```sql
select * from email order by id;
```

## Evidência esperada

Após cadastrar destinatários e solicitar envio em lote, a tabela `email` deve mostrar um registro para cada destinatário.

Exemplo:

```text
id | assunto            | destinatario      | status  | data_envio
---+--------------------+-------------------+---------+----------------------------
1  | Comunicado em Lote | joao@email.com    | ENVIADO | 2026-05-27 19:56:06.6211
2  | Comunicado em Lote | maria@email.com   | ENVIADO | 2026-05-27 19:56:06.644772
3  | Comunicado em Lote | carlos@email.com  | ENVIADO | 2026-05-27 19:56:06.656271
```

No RabbitMQ, a fila `fila.email` deve voltar para `Ready = 0` depois que o consumer processar as mensagens.

## Frontend

A interface permite:

- Cadastrar destinatários;
- Visualizar destinatários cadastrados;
- Enviar mensagem em lote;
- Visualizar envios realizados;
- Ver status dos envios;
- Expandir um envio para ver a mensagem enviada.

A tela consome a API do backend em:

```text
http://localhost:8080
```

## Observação sobre o envio de e-mails

Nesta versão acadêmica, o envio de e-mails é simulado no console pelo consumer.

O objetivo principal é demonstrar o fluxo assíncrono com RabbitMQ, incluindo:

- Producer;
- Exchange;
- Routing Key;
- Queue;
- Consumer;
- Banco de dados;
- Atualização de status.

Em uma evolução futura, o consumer poderia ser integrado a um serviço real de envio de e-mails, como SMTP, Mailtrap, Ethereal Email, SendGrid ou outro provedor.

## Limitações desta versão

Esta versão foi mantida simples para atender ao escopo principal da atividade.

Não foram implementados nesta etapa:

- Retry automático;
- Dead Letter Queue;
- Idempotência;
- Microsserviços separados;
- Envio real por SMTP;
- Autenticação de usuários.

Esses pontos podem ser adicionados futuramente.

## Como parar os containers

Na raiz do projeto:

```bash
docker compose -p email-rabbit down
```

Para apagar também os dados dos volumes:

```bash
docker compose -p email-rabbit down -v
```

## Integrantes

- Nome do integrante 1
- Nome do integrante 2
- Nome do integrante 3
- Nome do integrante 4

## Status do projeto

Projeto funcional para entrega acadêmica, demonstrando o uso de RabbitMQ com Java, Spring Boot, PostgreSQL e React.