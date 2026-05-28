package com.trabalho.emailrabbitmq.repository;

import com.trabalho.emailrabbitmq.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {
}
