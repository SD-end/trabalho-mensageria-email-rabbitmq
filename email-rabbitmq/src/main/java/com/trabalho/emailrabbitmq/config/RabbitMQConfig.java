package com.trabalho.emailrabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    public static final String EXCHANGE_EMAIL = "email.exchange";
    public static final String FILA_EMAIL = "fila.email";
    public static final String ROUTING_KEY_EMAIL = "email.enviar";

    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange(EXCHANGE_EMAIL, true, false);
    }

    @Bean
    public Queue filaEmail() {
        return new Queue(FILA_EMAIL, true);
    }

    @Bean
    public Binding bindingEmail(Queue filaEmail, DirectExchange emailExchange) {
        return BindingBuilder
                .bind(filaEmail)
                .to(emailExchange)
                .with(ROUTING_KEY_EMAIL);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    @Bean
    public ApplicationRunner inicializarRabbitMQ(
            RabbitAdmin rabbitAdmin,
            Queue filaEmail,
            DirectExchange emailExchange,
            Binding bindingEmail
    ) {
        return args -> {
            rabbitAdmin.declareExchange(emailExchange);
            rabbitAdmin.declareQueue(filaEmail);
            rabbitAdmin.declareBinding(bindingEmail);
        };
    }
}