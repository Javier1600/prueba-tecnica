package com.banco.ms_clientes.services;

import com.banco.ms_clientes.dto.ClienteDTO;
import com.banco.ms_clientes.dto.response.RespuestaPorDefecto;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface ClienteService {

    /**
     *  Servicio para obtención de todos los cleientes
     */
    RespuestaPorDefecto<List<ClienteDTO>> obtenerClientes();

    /**
     *  Servicio para obtención de un cliente por id
     */
    RespuestaPorDefecto<ClienteDTO> obtenerCliente(Integer idCliente);

    /**
     * Servicio para creación de un cliente
     */
    RespuestaPorDefecto<ClienteDTO> crearCliente(@NotNull ClienteDTO cliente);

    /**
     * Servicio para editar de un cliente
     */
    RespuestaPorDefecto<ClienteDTO> editarCliente(@NotNull ClienteDTO cliente);

    /**
     *  Servicio para eliminar de un cliente por id
     */
    RespuestaPorDefecto<ClienteDTO> eliminarCliente(Integer idCliente);

}
