# Frontend - Sistema de Envio de E-mails em Lote

Este é o frontend do projeto **Sistema de Envio de E-mails em Lote com Java e RabbitMQ**.

A interface foi desenvolvida com React, TypeScript e Vite, consumindo a API do backend Spring Boot.

## Objetivo

O objetivo do frontend é permitir a interação visual com o sistema, facilitando a demonstração da atividade acadêmica.

Através da interface é possível:

- Cadastrar destinatários;
- Listar destinatários cadastrados;
- Criar uma mensagem;
- Solicitar envio em lote;
- Visualizar os envios realizados;
- Acompanhar o status dos envios;
- Expandir um envio para visualizar a mensagem enviada.

## Tecnologias utilizadas

- React
- TypeScript
- Vite
- CSS
- Fetch API

## Comunicação com o backend

O frontend consome a API do backend em:

```text
http://localhost:8080
```

Principais endpoints utilizados:

```text
GET  /destinatarios
POST /destinatarios

GET  /email
POST /email/lote
```

## Funcionalidades da tela

### Cadastro de destinatários

Permite cadastrar nome e e-mail de um destinatário.

Exemplo de envio para o backend:

```json
{
  "nome": "João",
  "email": "joao@email.com"
}
```

### Listagem de destinatários

Mostra os destinatários já cadastrados no banco de dados PostgreSQL.

### Envio em lote

Permite informar um assunto e uma mensagem.

Ao enviar, o backend busca todos os destinatários cadastrados, cria os registros de envio e publica as mensagens no RabbitMQ.

### Listagem de envios

Mostra os envios registrados no banco, incluindo:

- ID;
- Destinatário;
- Assunto;
- Status;
- Data de envio.

Também é possível clicar em um envio para visualizar a mensagem enviada.

## Como executar o frontend

Entre na pasta do frontend:

```bash
cd frontend-email
```

Instale as dependências:

```bash
npm install
```

Execute o projeto:

```bash
npm run dev
```

Caso esteja usando PowerShell e apareça erro de execução de scripts, use:

```bash
npm.cmd install
npm.cmd run dev
```

O frontend ficará disponível em:

```text
http://localhost:5173
```

## Requisitos para funcionar corretamente

Antes de usar o frontend, é necessário que:

1. O PostgreSQL esteja rodando via Docker;
2. O RabbitMQ esteja rodando via Docker;
3. O backend Spring Boot esteja rodando em `http://localhost:8080`.

## Observação

O frontend não se comunica diretamente com o RabbitMQ nem com o PostgreSQL.

A comunicação acontece assim:

```text
Frontend React
      ↓
Backend Spring Boot
      ↓
PostgreSQL + RabbitMQ
```

O RabbitMQ é utilizado internamente pelo backend para processar os envios de forma assíncrona.

## Estrutura principal

```text
frontend-email
├── public
├── src
│   ├── App.tsx
│   ├── App.css
│   └── main.tsx
├── index.html
├── package.json
├── package-lock.json
├── tsconfig.json
└── vite.config.ts
```

## Status

Frontend funcional para demonstração acadêmica do sistema de envio de e-mails em lote.