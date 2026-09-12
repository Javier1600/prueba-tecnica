package com.banco.ms_clientes.messaging;

import com.banco.ms_clientes.config.RabbitConfig;
import com.banco.ms_clientes.dto.events.ClienteEvent;
import com.banco.ms_clientes.entities.Cliente;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ClienteEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final Logger log = Logger.getLogger(String.valueOf(ClienteEventPublisher.class));

    public void publicarClienteCreado(Cliente cliente) {
        ClienteEvent event = new ClienteEvent(
                cliente.getIdPersona(),
                cliente.getNombre(),
                cliente.getIdentificacion(),
                cliente.getEstado()
        );

        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY_CLIENTE_CREADO,
                event
        );

        log.info("Evento publicado: " + event);
    }


    public void publicarClienteEditado(Cliente cliente) {
        ClienteEvent event = new ClienteEvent(
                cliente.getIdPersona(),
                cliente.getNombre(),
                cliente.getIdentificacion(),
                cliente.getEstado()
        );

        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY_CLIENTE_EDITADO,
                event
        );

        log.info("Evento publicado: " + event);
    }

    public void publicarClienteEliminado(Cliente cliente) {
        ClienteEvent event = new ClienteEvent(
                cliente.getIdPersona(),
                cliente.getNombre(),
                cliente.getIdentificacion(),
                cliente.getEstado()
        );

        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY_CLIENTE_ELIMINADO,
                event
        );

        log.info("Evento publicado: " + event);
    }
}
