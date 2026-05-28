package com.trabalho.emailrabbitmq.service;

import com.trabalho.emailrabbitmq.config.RabbitMQConfig;
import com.trabalho.emailrabbitmq.model.Email;
import com.trabalho.emailrabbitmq.repository.EmailRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service

public class EmailConsumer {

    private final EmailRepository emailRepository;

    public EmailConsumer(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.FILA_EMAIL)
    public void processarEmail(Long emailId) {
        Email email = emailRepository.findById(emailId)
                .orElseThrow(() -> new RuntimeException("Email não encontrado"));

        System.out.println("Enviando email para: " + email.getDestinatario());
        System.out.println("Assunto: " + email.getAssunto());
        System.out.println("Mensagem: " + email.getMensagem());

        email.setStatus("ENVIADO");
        email.setDataEnvio(LocalDateTime.now());

        emailRepository.save(email);

        System.out.println("Email enviado com sucesso! ID: " + email.getId());
    }
}
