package com.banco.ms_cuentas.messaging;

import com.banco.ms_cuentas.config.RabbitConfig;
import com.banco.ms_cuentas.dto.events.ClienteEvent;
import com.banco.ms_cuentas.entities.Cliente;
import com.banco.ms_cuentas.entities.Cuenta;
import com.banco.ms_cuentas.entities.MovimientoCuenta;
import com.banco.ms_cuentas.repositories.ClienteRepository;
import com.banco.ms_cuentas.repositories.CuentaRepository;
import com.banco.ms_cuentas.repositories.MovimientoCuentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ClienteEventListener {

    private final ClienteRepository clienteRepository;
    private final CuentaRepository cuentaRepository;
    private final MovimientoCuentaRepository movimientoCuentaRepository;
    private final Logger log = Logger.getLogger(ClienteEventListener.class.getName());

    @RabbitListener(queues = RabbitConfig.ROUTING_KEY_CLIENTE_CREADO)
    public void onClienteCreado(ClienteEvent event) {
        log.info("Evento recibido: " + event);

        Cliente cliente = new Cliente();
        cliente.setClienteId(event.getClienteId());
        cliente.setNombre(event.getNombre());
        cliente.setIdentificacion(event.getIdentificacion());
        cliente.setEstado(event.getEstado());
        cliente.setFechaSincronizacion(LocalDateTime.now());

        clienteRepository.save(cliente);
    }

    @RabbitListener(queues = RabbitConfig.ROUTING_KEY_CLIENTE_EDITADO)
    public void onClienteEditado(ClienteEvent event) {
        log.info("Evento recibido: " + event);

        Cliente cliente = clienteRepository.findByClienteIdAndEstado(event.getClienteId(), true);

        if(cliente != null) {
            cliente.setClienteId(event.getClienteId());
            cliente.setNombre(event.getNombre());
            cliente.setIdentificacion(event.getIdentificacion());
            cliente.setEstado(event.getEstado());
            cliente.setFechaSincronizacion(LocalDateTime.now());

            clienteRepository.save(cliente);
        }
    }

    @RabbitListener(queues = RabbitConfig.ROUTING_KEY_CLIENTE_ELIMINADO)
    public void onClienteEliminado(ClienteEvent event) {
        log.info("Evento recibido: " + event);

        Cliente cliente = clienteRepository.findByClienteIdAndEstado(event.getClienteId(), true);

        if(cliente != null) {
            cliente.setClienteId(event.getClienteId());
            cliente.setNombre(event.getNombre());
            cliente.setIdentificacion(event.getIdentificacion());
            cliente.setEstado(false);
            cliente.setFechaSincronizacion(LocalDateTime.now());

            clienteRepository.save(cliente);

            // Obtengo cuentas asociadas
            List<Cuenta> cuentasCliente = cuentaRepository.findByClienteClienteIdAndEstadoOrderByCuentaIdDesc(
                    cliente.getClienteId(), true
            );

            // Marco estado como inactivo a todas las cuentas
            for(Cuenta cuenta : cuentasCliente) {
                cuenta.setEstado(false);

                // Obtengo movimientos asociados a la cuenta
                List<MovimientoCuenta> movimientos = movimientoCuentaRepository
                        .findByCuentaCuentaIdAndEstadoOrderByMovimientoIdDesc(cuenta.getCuentaId(), true);

                // Marco como inactivos
                for(MovimientoCuenta movimiento : movimientos) {
                    movimiento.setEstado(false);
                }

                movimientoCuentaRepository.saveAll(movimientos);
            }

            cuentaRepository.saveAll(cuentasCliente);

        }
    }
}
