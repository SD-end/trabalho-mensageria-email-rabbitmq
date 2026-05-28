import { Fragment, useEffect, useState } from "react";
import "./App.css";

type Destinatario = {
  id: number;
  nome: string;
  email: string;
};

type EmailEnvio = {
  id: number;
  destinatario: string;
  assunto: string;
  mensagem: string;
  status: string;
  dataEnvio: string | null;
};

const API_URL = "http://localhost:8080";

function App() {
  const [nome, setNome] = useState("");
  const [email, setEmail] = useState("");
  const [assunto, setAssunto] = useState("");
  const [mensagem, setMensagem] = useState("");
  const [destinatarios, setDestinatarios] = useState<Destinatario[]>([]);
  const [envios, setEnvios] = useState<EmailEnvio[]>([]);
  const [aviso, setAviso] = useState("");
  const [envioAbertoId, setEnvioAbertoId] = useState<number | null>(null);

  async function carregarDestinatarios() {
    const resposta = await fetch(`${API_URL}/destinatarios`);
    const dados = await resposta.json();
    setDestinatarios(dados);
  }

  async function carregarEnvios() {
    const resposta = await fetch(`${API_URL}/email`);
    const dados = await resposta.json();
    setEnvios(dados);
  }

  async function cadastrarDestinatario(event: React.FormEvent) {
    event.preventDefault();

    await fetch(`${API_URL}/destinatarios`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ nome, email }),
    });

    setNome("");
    setEmail("");
    setAviso("Destinatário cadastrado com sucesso.");
    carregarDestinatarios();
  }

  async function enviarEmLote(event: React.FormEvent) {
    event.preventDefault();

    await fetch(`${API_URL}/email/lote`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ assunto, mensagem }),
    });

    setAssunto("");
    setMensagem("");
    setAviso("Envio em lote solicitado. O consumer processará as mensagens pela fila.");

    setTimeout(() => {
      carregarEnvios();
    }, 1000);
  }

  useEffect(() => {
    carregarDestinatarios();
    carregarEnvios();
  }, []);

  return (
    <div className="container">
      <header className="cabecalho">
        <h1>Sistema de Envio de E-mails em Lote</h1>
        <p>Java + Spring Boot + RabbitMQ + PostgreSQL</p>
      </header>

      {aviso && <div className="aviso">{aviso}</div>}

      <main className="grid">
        <section className="card">
          <h2>Cadastrar destinatário</h2>

          <form onSubmit={cadastrarDestinatario}>
            <label>Nome</label>
            <input
              type="text"
              placeholder="Digite o nome do destinatário:"
              value={nome}
              onChange={(e) => setNome(e.target.value)}
              required
            />

            <label>E-mail</label>
            <input
              type="email"
              placeholder="Digite o e-mail do destinatário:"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />

            <button type="submit">Cadastrar</button>
          </form>
        </section>

        <section className="card">
          <h2>Enviar mensagem em lote</h2>

          <form onSubmit={enviarEmLote}>
            <label>Assunto</label>
            <input
              type="text"
              placeholder="Digite o assunto da mensagem:"
              value={assunto}
              onChange={(e) => setAssunto(e.target.value)}
              required
            />

            <label>Mensagem</label>
            <textarea
              placeholder="Digite a mensagem:"
              value={mensagem}
              onChange={(e) => setMensagem(e.target.value)}
              required
            />

            <button type="submit">Enviar para todos</button>
          </form>
        </section>
      </main>

      <section className="card">
        <h2>Destinatários cadastrados</h2>

        {destinatarios.length === 0 ? (
          <p>Nenhum destinatário cadastrado.</p>
        ) : (
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>E-mail</th>
              </tr>
            </thead>
            <tbody>
              {destinatarios.map((destinatario) => (
                <tr key={destinatario.id}>
                  <td>{destinatario.id}</td>
                  <td>{destinatario.nome}</td>
                  <td>{destinatario.email}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </section>

      <section className="card">
        <h2>Envios realizados</h2>

        <button className="botao-secundario" onClick={carregarEnvios}>
          Atualizar envios
        </button>

        {envios.length === 0 ? (
          <p>Nenhum envio registrado.</p>
        ) : (
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Destinatário</th>
                <th>Assunto</th>
                <th>Status</th>
                <th>Data de envio</th>
              </tr>
            </thead>
              <tbody>
                {envios.map((envio) => (
                  <Fragment key={envio.id}>
                    <tr
                      className="linha-envio"
                      onClick={() =>
                        setEnvioAbertoId(envioAbertoId === envio.id ? null : envio.id)
                      }
                    >
                      <td>{envio.id}</td>
                      <td>{envio.destinatario}</td>
                      <td>{envio.assunto}</td>
                      <td>
                        <span className={`status ${envio.status.toLowerCase()}`}>
                          {envio.status}
                        </span>
                      </td>
                      <td>{envio.dataEnvio ?? "Ainda não processado"}</td>
                    </tr>

                    {envioAbertoId === envio.id && (
                      <tr>
                        <td colSpan={5} className="detalhe-mensagem">
                          <strong>Mensagem enviada:</strong>
                          <p>{envio.mensagem}</p>
                        </td>
                      </tr>
                    )}
                  </Fragment>
                ))}
            </tbody>
          </table>
        )}
      </section>
    </div>
  );
}

export default App;