package com.trabalho.emailrabbitmq.repository;

import com.trabalho.emailrabbitmq.model.Destinatario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DestinatarioRepository extends JpaRepository<Destinatario, Long> {
}
