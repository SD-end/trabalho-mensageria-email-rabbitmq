package com.trabalho.emailrabbitmq.controller;

import com.trabalho.emailrabbitmq.dto.DestinatarioRequest;
import com.trabalho.emailrabbitmq.model.Destinatario;
import com.trabalho.emailrabbitmq.service.DestinatarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/destinatarios")
public class DestinatarioController {

    private final DestinatarioService destinatarioService;

    public DestinatarioController(DestinatarioService destinatarioService) {
        this.destinatarioService = destinatarioService;
    }

    @PostMapping
    public ResponseEntity<Destinatario> cadastrar(@RequestBody @Valid DestinatarioRequest request) {
        Destinatario destinatario = destinatarioService.cadastrar(request);
        return ResponseEntity.ok(destinatario);
    }

    @GetMapping
    public ResponseEntity<List<Destinatario>> listarTodos() {
        return ResponseEntity.ok(destinatarioService.listarTodos());
    }
}