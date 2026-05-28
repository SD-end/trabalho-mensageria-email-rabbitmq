package com.trabalho.emailrabbitmq.dto;

import jakarta.validation.constraints.NotBlank;

public class EnvioLoteRequest {

    @NotBlank
    private String assunto;

    @NotBlank
    private String mensagem;

    public String getAssunto() {
        return assunto;
    }

    public String getMensagem() {
        return mensagem;
    }
}