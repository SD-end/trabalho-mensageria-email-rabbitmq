package com.trabalho.emailrabbitmq.service;

import com.trabalho.emailrabbitmq.config.RabbitMQConfig;
import com.trabalho.emailrabbitmq.dto.EmailRequest;
import com.trabalho.emailrabbitmq.dto.EnvioLoteRequest;
import com.trabalho.emailrabbitmq.model.Destinatario;
import com.trabalho.emailrabbitmq.model.Email;
import com.trabalho.emailrabbitmq.repository.DestinatarioRepository;
import com.trabalho.emailrabbitmq.repository.EmailRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {

    private final EmailRepository emailRepository;
    private final DestinatarioRepository destinatarioRepository;
    private final RabbitTemplate rabbitTemplate;

    public EmailService(
            EmailRepository emailRepository,
            DestinatarioRepository destinatarioRepository,
            RabbitTemplate rabbitTemplate
    ) {
        this.emailRepository = emailRepository;
        this.destinatarioRepository = destinatarioRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Email solicitarEnvio(EmailRequest request) {
        Email email = new Email();
        email.setDestinatario(request.getDestinatario());
        email.setAssunto(request.getAssunto());
        email.setMensagem(request.getMensagem());
        email.setStatus("PENDENTE");

        Email emailSalvo = emailRepository.save(email);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_EMAIL,
                RabbitMQConfig.ROUTING_KEY_EMAIL,
                emailSalvo.getId()
        );

        return emailSalvo;
    }

    public List<Email> enviarEmLote(EnvioLoteRequest request) {
        List<Destinatario> destinatarios = destinatarioRepository.findAll();

        if (destinatarios.isEmpty()) {
            throw new RuntimeException("Nenhum destinatário cadastrado.");
        }

        List<Email> emailsCriados = new ArrayList<>();

        for (Destinatario destinatario : destinatarios) {
            Email email = new Email();
            email.setDestinatario(destinatario.getEmail());
            email.setAssunto(request.getAssunto());
            email.setMensagem(request.getMensagem());
            email.setStatus("PENDENTE");

            Email emailSalvo = emailRepository.save(email);

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_EMAIL,
                    RabbitMQConfig.ROUTING_KEY_EMAIL,
                    emailSalvo.getId()
            );

            emailsCriados.add(emailSalvo);
        }

        return emailsCriados;
    }

    public List<Email> listarTodos() {
        return emailRepository.findAll();
    }

    public Email buscarPorId(Long id) {
        return emailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("E-mail não encontrado"));
    }
}