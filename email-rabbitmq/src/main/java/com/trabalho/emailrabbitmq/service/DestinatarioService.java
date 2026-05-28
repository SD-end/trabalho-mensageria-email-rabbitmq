package com.trabalho.emailrabbitmq.service;

import com.trabalho.emailrabbitmq.dto.DestinatarioRequest;
import com.trabalho.emailrabbitmq.model.Destinatario;
import com.trabalho.emailrabbitmq.repository.DestinatarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinatarioService {

    private final DestinatarioRepository destinatarioRepository;

    public DestinatarioService(DestinatarioRepository destinatarioRepository) {
        this.destinatarioRepository = destinatarioRepository;

    }

    public Destinatario cadastrar(DestinatarioRequest request){
        Destinatario destinatario = new Destinatario();
        destinatario.setNome(request.getNome());
        destinatario.setEmail(request.getEmail());

        return destinatarioRepository.save(destinatario);
    }

    public List<Destinatario> listarTodos(){
        return destinatarioRepository.findAll();
    }

}
