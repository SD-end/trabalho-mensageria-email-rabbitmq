package com.trabalho.emailrabbitmq.controller;

import com.trabalho.emailrabbitmq.dto.EmailRequest;
import com.trabalho.emailrabbitmq.dto.EnvioLoteRequest;
import com.trabalho.emailrabbitmq.model.Email;
import com.trabalho.emailrabbitmq.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/email")

public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService){
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<Email> solicitarEnvio(@RequestBody @Valid EmailRequest request) {
        Email email = emailService.solicitarEnvio(request);
        return ResponseEntity.accepted().body(email);
    }

    @GetMapping
    public ResponseEntity<List<Email>> listarTodos() {
        return ResponseEntity.ok(emailService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Email> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(emailService.buscarPorId(id));
    }

    @PostMapping("/lote")
    public ResponseEntity<List<Email>> enviarEmLote(@RequestBody @Valid EnvioLoteRequest request) {
        List<Email> emails = emailService.enviarEmLote(request);
        return ResponseEntity.accepted().body(emails);
    }

}
