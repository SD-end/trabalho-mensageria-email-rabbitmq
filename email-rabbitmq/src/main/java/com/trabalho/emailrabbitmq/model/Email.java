package com.trabalho.emailrabbitmq.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalDateTime.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "email")

public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String destinatario;

    private String assunto;

    @Column(columnDefinition = "TEXT")
    private String mensagem;

    private String status;

    private LocalDateTime dataEnvio;

}
