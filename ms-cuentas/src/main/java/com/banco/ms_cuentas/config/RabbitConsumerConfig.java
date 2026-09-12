package com.banco.ms_cuentas.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConsumerConfig {

    @Bean
    public Queue clienteCreadoQueue() {
        return QueueBuilder.durable(RabbitConfig.ROUTING_KEY_CLIENTE_CREADO).build();
    }

    @Bean
    public Queue clienteEditadoQueue() {
        return QueueBuilder.durable(RabbitConfig.ROUTING_KEY_CLIENTE_EDITADO).build();
    }

    @Bean
    public Queue clienteEliminadoQueue() {
        return QueueBuilder.durable(RabbitConfig.ROUTING_KEY_CLIENTE_ELIMINADO).build();
    }

    @Bean
    public Binding bindingClienteCreado(Queue clienteCreadoQueue,
                                        TopicExchange clientesExchange) {
        return BindingBuilder.bind(clienteCreadoQueue)
                .to(clientesExchange)
                .with(RabbitConfig.ROUTING_KEY_CLIENTE_CREADO);
    }

    @Bean
    public Binding bindingClienteEditado(Queue clienteEditadoQueue,
                                        TopicExchange clientesExchange) {
        return BindingBuilder.bind(clienteEditadoQueue)
                .to(clientesExchange)
                .with(RabbitConfig.ROUTING_KEY_CLIENTE_EDITADO);
    }

    @Bean
    public Binding bindingClienteEliminado(Queue clienteEliminadoQueue,
                                         TopicExchange clientesExchange) {
        return BindingBuilder.bind(clienteEliminadoQueue)
                .to(clientesExchange)
                .with(RabbitConfig.ROUTING_KEY_CLIENTE_ELIMINADO);
    }
}
