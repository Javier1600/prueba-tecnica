package com.banco.ms_cuentas.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "clientes.exchange";
    public static final String ROUTING_KEY_CLIENTE_CREADO = "cliente.creado";
    public static final String ROUTING_KEY_CLIENTE_EDITADO = "cliente.editado";
    public static final String ROUTING_KEY_CLIENTE_ELIMINADO = "cliente.eliminado";

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public TopicExchange clientesExchange() {
        return new TopicExchange(EXCHANGE);
    }
}