# Sistema de Envio de E-mails em Lote com Java e RabbitMQ

Este projeto é um backend desenvolvido em Java com Spring Boot para realizar o envio de e-mails em lote de forma assíncrona utilizando RabbitMQ.

O sistema permite cadastrar destinatários no banco de dados, listar os destinatários cadastrados, criar uma mensagem e solicitar o envio dessa mensagem para todos os e-mails registrados. O envio não é processado diretamente na requisição principal: a aplicação publica mensagens no RabbitMQ e um consumidor processa essas mensagens posteriormente.

## Objetivo do projeto

O objetivo é aplicar, na prática, os conceitos de mensageria com RabbitMQ, utilizando:

- Producer
- Exchange
- Queue
- Binding
- Routing Key
- Consumer
- RabbitTemplate
- @RabbitListener
- Banco de dados PostgreSQL
- Processamento assíncrono

## Relação com a documentação de arquitetura

Este projeto foi desenvolvido com base nos conceitos apresentados na documentação inicial de mensageria confiável.

Na documentação, a arquitetura completa utiliza RabbitMQ para permitir comunicação assíncrona entre serviços, com uso de exchange, filas, bindings, routing keys, producers e consumers. Também são discutidos conceitos como confiabilidade, roteamento flexível, acknowledgements, retry, DLQ e idempotência.

Nesta etapa da entrega, foi implementada uma versão essencial e funcional focada no envio de e-mails em lote. O sistema contempla os principais elementos obrigatórios da atividade:

- Cadastro de destinatários no banco de dados;
- Criação de mensagem para envio;
- Publicação da solicitação de envio no RabbitMQ;
- Uso de exchange, fila, binding e routing key;
- Producer publicando mensagens;
- Consumer processando mensagens com `@RabbitListener`;
- Atualização do status dos envios no banco de dados.

Assim, o projeto não implementa toda a arquitetura completa proposta na documentação, mas entrega uma parte funcional dela, focada no fluxo assíncrono de envio de e-mails.

## Funcionalidades implementadas

- Cadastro de destinatários no banco de dados
- Listagem dos destinatários cadastrados
- Criação de mensagem para envio
- Solicitação de envio em lote
- Publicação das mensagens no RabbitMQ
- Uso de exchange, fila, binding e routing key
- Consumo das mensagens da fila
- Simulação do envio de e-mails
- Atualização do status do envio no banco
- Evidência de processamento por logs, RabbitMQ e PostgreSQL

## Tecnologias utilizadas

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Bean Validation
- RabbitMQ
- PostgreSQL
- Docker
- Docker Compose
- Maven
- Postman

## Como as tecnologias são usadas

### Spring Boot

Usado para criar e executar a aplicação backend.

Ele inicializa o servidor web, configura as dependências e integra os recursos de banco de dados, validação e RabbitMQ.

### Spring Web

Usado para criar os endpoints REST da aplicação.

Endpoints principais:

- `POST /destinatarios`
- `GET /destinatarios`
- `POST /email`
- `POST /email/lote`
- `GET /email`
- `GET /email/{id}`

### Spring Data JPA

Usado para acessar o banco de dados PostgreSQL através de repositories.

Repositories principais:

- `DestinatarioRepository`
- `EmailRepository`

Eles permitem salvar, listar e buscar dados sem escrever SQL manualmente para operações simples.

### Bean Validation

Usado para validar os dados recebidos nas requisições.

Exemplos de validações utilizadas:

- `@NotBlank`
- `@Email`

Com isso, campos obrigatórios não podem ser enviados vazios e campos de e-mail precisam ter formato válido.

### PostgreSQL

Banco de dados usado para armazenar os destinatários cadastrados e os registros de envio.

Tabelas principais:

- `destinatarios`
- `email`

### RabbitMQ

Broker de mensagens usado para realizar o processamento assíncrono.

A aplicação usa:

```text
Exchange: email.exchange
Tipo: direct

Fila: fila.email

Routing key: email.enviar
```

O producer publica mensagens na exchange `email.exchange` usando a routing key `email.enviar`.

A exchange encaminha as mensagens para a fila `fila.email`.

O consumer escuta a fila `fila.email`, processa a mensagem e atualiza o status do envio no banco de dados.

### Docker

Usado para executar o PostgreSQL e o RabbitMQ em containers.

### Docker Compose

Usado para subir o PostgreSQL e o RabbitMQ com um único comando.

## Como executar o projeto

### 1. Subir os containers

Na raiz do projeto, execute:

```bash
docker compose -p email-rabbit up -d
```

Esse comando sobe os containers do PostgreSQL e RabbitMQ.

### 2. Acessar o RabbitMQ

Abra no navegador:

```text
http://localhost:15673
```

Login:

```text
Usuário: guest
Senha: guest
```

### 3. Executar o backend

Execute a classe principal:

```text
EmailRabbitmqApplication.java
```

A aplicação será iniciada em:

```text
http://localhost:8080
```

## Configuração do application.properties

O arquivo `application.properties` deve conter:

```properties
spring.application.name=sistema-email-lote

server.port=8080

spring.datasource.url=jdbc:postgresql://localhost:5433/email_db
spring.datasource.username=email_user
spring.datasource.password=email_pass
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

spring.rabbitmq.host=localhost
spring.rabbitmq.port=5673
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

## Endpoints da API

## Destinatários

### Cadastrar destinatário

```http
POST /destinatarios
```

URL completa:

```text
http://localhost:8080/destinatarios
```

Exemplo de corpo:

```json
{
  "nome": "João",
  "email": "joao@email.com"
}
```

Exemplo de resposta:

```json
{
  "id": 1,
  "nome": "João",
  "email": "joao@email.com"
}
```

### Listar destinatários

```http
GET /destinatarios
```

URL completa:

```text
http://localhost:8080/destinatarios
```

Exemplo de resposta:

```json
[
  {
    "id": 1,
    "nome": "João",
    "email": "joao@email.com"
  },
  {
    "id": 2,
    "nome": "Maria",
    "email": "maria@email.com"
  }
]
```

## E-mails

### Enviar e-mail individual

```http
POST /email
```

URL completa:

```text
http://localhost:8080/email
```

Exemplo de corpo:

```json
{
  "destinatario": "teste@email.com",
  "assunto": "Teste RabbitMQ",
  "mensagem": "Mensagem enviada pelo Postman"
}
```

Esse endpoint cria um envio individual, salva com status `PENDENTE`, publica a mensagem no RabbitMQ e o consumer processa o envio.

### Enviar e-mails em lote

```http
POST /email/lote
```

URL completa:

```text
http://localhost:8080/email/lote
```

Exemplo de corpo:

```json
{
  "assunto": "Comunicado da Loja Virtual",
  "mensagem": "Olá, temos uma nova campanha disponível para nossos clientes."
}
```

Funcionamento:

1. A API recebe o assunto e a mensagem.
2. O sistema busca todos os destinatários cadastrados.
3. Para cada destinatário, cria um registro na tabela `email` com status `PENDENTE`.
4. Para cada registro criado, publica uma mensagem no RabbitMQ.
5. O consumer processa cada mensagem.
6. O status de cada envio é atualizado para `ENVIADO`.

Exemplo de resposta:

```json
[
  {
    "id": 4,
    "destinatario": "joao@email.com",
    "assunto": "Comunicado da Loja Virtual",
    "mensagem": "Olá, temos uma nova campanha disponível para nossos clientes.",
    "status": "PENDENTE",
    "dataEnvio": null
  },
  {
    "id": 5,
    "destinatario": "maria@email.com",
    "assunto": "Comunicado da Loja Virtual",
    "mensagem": "Olá, temos uma nova campanha disponível para nossos clientes.",
    "status": "PENDENTE",
    "dataEnvio": null
  }
]
```

Após o consumer processar as mensagens, os registros são atualizados para `ENVIADO`.

### Listar envios de e-mail

```http
GET /email
```

URL completa:

```text
http://localhost:8080/email
```

Exemplo de resposta:

```json
[
  {
    "id": 1,
    "destinatario": "teste@email.com",
    "assunto": "Teste RabbitMQ",
    "mensagem": "Mensagem enviada pelo Postman",
    "status": "ENVIADO",
    "dataEnvio": "2026-05-25T20:34:27.890367"
  }
]
```

### Buscar envio por ID

```http
GET /email/{id}
```

Exemplo:

```text
http://localhost:8080/email/1
```

## Fluxo do envio em lote

O fluxo principal do sistema é:

```text
Cadastro de destinatários
        ↓
Criação da mensagem
        ↓
POST /email/lote
        ↓
Busca dos destinatários no PostgreSQL
        ↓
Criação dos registros com status PENDENTE
        ↓
Producer publica mensagens na exchange email.exchange
        ↓
Exchange usa a routing key email.enviar
        ↓
Mensagem chega na fila fila.email
        ↓
Consumer processa a mensagem
        ↓
Status atualizado para ENVIADO
```

## Configuração do RabbitMQ

A configuração principal do RabbitMQ fica na classe:

```text
RabbitMQConfig.java
```

Ela configura:

```text
Exchange: email.exchange
Fila: fila.email
Routing key: email.enviar
Binding: email.exchange -> fila.email usando email.enviar
```

A exchange utilizada é do tipo:

```text
direct
```

Isso significa que a mensagem é roteada para a fila quando a routing key usada pelo producer corresponde à routing key configurada no binding.

## Producer

O producer é responsável por publicar mensagens no RabbitMQ.

No projeto, a publicação é feita usando:

```text
RabbitTemplate
```

A mensagem é enviada para:

```text
Exchange: email.exchange
Routing key: email.enviar
```

O conteúdo enviado é o ID do registro de e-mail criado no banco.

## Consumer

O consumer é responsável por escutar a fila e processar as mensagens.

No projeto, ele utiliza:

```text
@RabbitListener
```

A fila escutada é:

```text
fila.email
```

Quando uma mensagem chega, o consumer:

1. Recebe o ID do e-mail.
2. Busca o registro no banco.
3. Simula o envio no console.
4. Atualiza o status para `ENVIADO`.
5. Preenche a data de envio.

## Status do envio

O projeto utiliza dois status principais:

### PENDENTE

Indica que o envio foi criado no banco e a mensagem foi publicada no RabbitMQ.

### ENVIADO

Indica que o consumer processou a mensagem da fila e atualizou o registro no banco.

## Como testar no Postman

### 1. Cadastrar destinatários

Faça algumas requisições:

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

Outros exemplos:

```json
{
  "nome": "Maria",
  "email": "maria@email.com"
}
```

```json
{
  "nome": "Carlos",
  "email": "carlos@email.com"
}
```

### 2. Listar destinatários

```http
GET http://localhost:8080/destinatarios
```

### 3. Solicitar envio em lote

```http
POST http://localhost:8080/email/lote
```

Body:

```json
{
  "assunto": "Comunicado da Loja Virtual",
  "mensagem": "Olá, temos uma nova campanha disponível para nossos clientes."
}
```

### 4. Listar envios

```http
GET http://localhost:8080/email
```

## Evidência de funcionamento no PostgreSQL

Para acessar o PostgreSQL do container:

```bash
docker exec -it postgres-email psql -U email_user -d email_db
```

Para listar as tabelas:

```sql
\dt
```

Resultado esperado:

```text
destinatarios
email
```

Para consultar os destinatários:

```sql
select * from destinatarios;
```

Para consultar os envios:

```sql
select * from email order by id;
```

Resultado esperado após o envio em lote:

- Um registro para cada destinatário cadastrado.
- Status atualizado para `ENVIADO`.
- Campo `data_envio` preenchido.

Exemplo:

```text
id | assunto                    | data_envio                  | destinatario      | mensagem                                                     | status
---+----------------------------+-----------------------------+-------------------+--------------------------------------------------------------+---------
4  | Comunicado da Loja Virtual | 2026-05-27 19:56:06.6211    | joao@email.com    | Olá, temos uma nova campanha disponível para nossos clientes. | ENVIADO
5  | Comunicado da Loja Virtual | 2026-05-27 19:56:06.644772  | maria@email.com   | Olá, temos uma nova campanha disponível para nossos clientes. | ENVIADO
6  | Comunicado da Loja Virtual | 2026-05-27 19:56:06.656271  | carlos@email.com  | Olá, temos uma nova campanha disponível para nossos clientes. | ENVIADO
```

## Evidência de funcionamento no RabbitMQ

Acesse:

```text
http://localhost:15673
```

Na aba `Exchanges`, é possível verificar:

```text
email.exchange
```

Dentro da exchange, deve existir o binding:

```text
Routing key: email.enviar
Fila: fila.email
```

Na aba `Queues and Streams`, é possível verificar a fila:

```text
fila.email
```

Durante o envio, as mensagens podem aparecer como `Ready`.

Após o consumer processar as mensagens, o campo `Ready` deve voltar para zero.

## Estrutura principal do projeto

```text
src/main/java/com/trabalho/emailrabbitmq
│
├── config
│   ├── RabbitMQConfig.java
│   └── CorsConfig.java
│
├── controller
│   ├── DestinatarioController.java
│   └── EmailController.java
│
├── dto
│   ├── DestinatarioRequest.java
│   ├── EmailRequest.java
│   └── EnvioLoteRequest.java
│
├── model
│   ├── Destinatario.java
│   └── Email.java
│
├── repository
│   ├── DestinatarioRepository.java
│   └── EmailRepository.java
│
├── service
│   ├── DestinatarioService.java
│   ├── EmailService.java
│   └── EmailConsumer.java
│
└── EmailRabbitmqApplication.java
```

## Principais classes

### RabbitMQConfig

Configura a exchange, fila, binding, routing key e inicialização dos componentes no RabbitMQ.

### DestinatarioController

Expõe os endpoints para cadastrar e listar destinatários.

### EmailController

Expõe os endpoints para envio individual, envio em lote, listagem e busca por ID.

### DestinatarioService

Contém a lógica de cadastro e listagem dos destinatários.

### EmailService

Contém a lógica de criação dos envios e publicação das mensagens no RabbitMQ.

### EmailConsumer

Escuta a fila `fila.email`, processa as mensagens e atualiza o status dos envios.

### EmailRepository

Responsável pelas operações de banco relacionadas aos registros de envio.

### DestinatarioRepository

Responsável pelas operações de banco relacionadas aos destinatários.

## Comandos úteis do Docker

Subir os containers:

```bash
docker compose -p email-rabbit up -d
```

Parar os containers:

```bash
docker compose -p email-rabbit down
```

Parar os containers e apagar os dados dos volumes:

```bash
docker compose -p email-rabbit down -v
```

Listar containers em execução:

```bash
docker ps
```

Acessar o PostgreSQL do container:

```bash
docker exec -it postgres-email psql -U email_user -d email_db
```

## Observação sobre o envio

Nesta versão acadêmica, o envio de e-mails é simulado no console pelo consumer.

O objetivo principal é demonstrar o fluxo assíncrono com RabbitMQ, incluindo producer, exchange, routing key, fila, consumer e persistência no banco de dados.

Em uma evolução futura, o consumer poderia ser integrado a um serviço real de envio de e-mails, como SMTP, Mailtrap, Ethereal Email, SendGrid ou outro provedor equivalente.

## Limitações desta etapa

Esta versão foi mantida simples para atender ao escopo principal da atividade acadêmica.

Atualmente, o envio de e-mails é simulado no console pelo consumer. A integração com um serviço real de e-mail, como Mailtrap, Ethereal Email, SMTP ou outro provedor, pode ser adicionada em uma etapa futura.

Também podem ser adicionados futuramente recursos mais avançados discutidos na documentação de arquitetura, como:

- Retry automático;
- Dead Letter Queue;
- Controle de idempotência;
- Monitoramento mais detalhado;
- Separação em múltiplos microsserviços.

Mesmo sem esses recursos avançados, esta versão já demonstra o fluxo principal de mensageria com RabbitMQ, separando a requisição inicial do processamento assíncrono.

## Autor

Projeto desenvolvido para atividade acadêmica de mensageria com Java e RabbitMQ.

Integrantes:

- Nome do integrante 1
- Nome do integrante 2
- Nome do integrante 3
- Nome do integrante 4