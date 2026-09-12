package com.banco.ms_clientes.services;

import com.banco.ms_clientes.dto.ClienteDTO;
import com.banco.ms_clientes.dto.response.RespuestaPorDefecto;
import com.banco.ms_clientes.entities.Cliente;
import com.banco.ms_clientes.messaging.ClienteEventPublisher;
import com.banco.ms_clientes.repositories.ClienteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteEventPublisher eventPublisher;

    @Override
    @Transactional(readOnly = true)
    public RespuestaPorDefecto<List<ClienteDTO>> obtenerClientes() {
        RespuestaPorDefecto<List<ClienteDTO>> respuesta = new RespuestaPorDefecto<>();

        try {
            List<ClienteDTO> clientes = clienteRepository.findAll()
                    .stream()
                    .map(Cliente::toDTO)
                    .toList();

            if(clientes.isEmpty()){
                respuesta.setMensaje("No se encontraron clientes registrados");
                return respuesta;
            }

            respuesta.llenarRespuestaExitosa("Clientes obtenidos correctamente", clientes);
        } catch (Exception ex) {
            respuesta.llenarConDatosDeException(ex);
        }

        return respuesta;
    }

    @Override
    @Transactional(readOnly = true)
    public RespuestaPorDefecto<ClienteDTO> obtenerCliente(Integer idCliente) {
        RespuestaPorDefecto<ClienteDTO> respuesta = new RespuestaPorDefecto<>();

        try {
            Cliente cliente = clienteRepository.findByIdPersonaAndEstado(idCliente.longValue(), true);

            if(cliente == null) {
                respuesta.setMensaje("Cliente encontrado");
                return respuesta;
            }

            respuesta.llenarRespuestaExitosa("Cliente encontrado", cliente.toDTO());
        } catch (Exception ex) {
            respuesta.llenarConDatosDeException(ex);
        }

        return respuesta;
    }

    @Override
    @Transactional
    public RespuestaPorDefecto<ClienteDTO> crearCliente(ClienteDTO clienteDTO)  {
        RespuestaPorDefecto<ClienteDTO> respuesta = new RespuestaPorDefecto<>();

        try {
            // Validar identificación obligatoria
            if (clienteDTO == null || clienteDTO.getIdentificacion() == null) {
                respuesta.setMensaje("No se recibió la identificación del cliente");
                return respuesta;
            }

            Cliente cliente = clienteRepository.findByIdentificacionAndEstado(
                    clienteDTO.getIdentificacion(), true
            );


            if(cliente != null) {
                respuesta.setMensaje(
                        "El cliente con la identificación " + clienteDTO.getIdentificacion()  + " ya existe"
                );
                return respuesta;
            }

            cliente = new Cliente();
            cliente.setPassword(clienteDTO.getPassword());
            cliente.setEstado(clienteDTO.isEstado());

            cliente.setNombre(clienteDTO.getNombre());
            cliente.setGenero(clienteDTO.getGenero());
            cliente.setEdad(clienteDTO.getEdad());
            cliente.setIdentificacion(clienteDTO.getIdentificacion());
            cliente.setDireccion(clienteDTO.getDireccion());
            cliente.setTelefono(clienteDTO.getTelefono());


            Cliente guardado = clienteRepository.save(cliente);

            // Publicar evento
            eventPublisher.publicarClienteCreado(guardado);

            respuesta.llenarRespuestaExitosa("Cliente creado correctamente", guardado.toDTO());

        } catch (Exception ex) {
            respuesta.llenarConDatosDeException(ex);
        }

        return respuesta;
    }

    @Override
    @Transactional
    public RespuestaPorDefecto<ClienteDTO> editarCliente(ClienteDTO clienteDTO)  {
        RespuestaPorDefecto<ClienteDTO> respuesta = new RespuestaPorDefecto<>();

        try {

            if (clienteDTO == null || clienteDTO.getIdPersona() == null) {
                respuesta.setMensaje("No se recibió el identificador del cliente");
                return respuesta;
            }

            // Validar que la identificación se reciba
            if (clienteDTO.getIdentificacion() == null) {
                respuesta.setMensaje("No se recibió la identificación del cliente");
                return respuesta;
            }

            Cliente cliente = clienteRepository.findByIdentificacionAndEstado(
                    clienteDTO.getIdentificacion(), true
            );

            if(cliente != null && !cliente.getIdPersona().equals(clienteDTO.getIdPersona())) {
                respuesta.setMensaje(
                        "El cliente con la identificación " + clienteDTO.getIdentificacion()  + " ya existe"
                );
                return respuesta;
            }

            cliente = clienteRepository.findByIdPersonaAndEstado(clienteDTO.getIdPersona(), true);

            if(cliente == null) {
                respuesta.setMensaje("No se ha encontrado el cliente con el id " + clienteDTO.getIdPersona());
                return respuesta;
            }

            cliente.setPassword(clienteDTO.getPassword());
            cliente.setEstado(clienteDTO.isEstado());

            cliente.setNombre(clienteDTO.getNombre());
            cliente.setGenero(clienteDTO.getGenero());
            cliente.setEdad(clienteDTO.getEdad());
            cliente.setIdentificacion(clienteDTO.getIdentificacion());
            cliente.setDireccion(clienteDTO.getDireccion());
            cliente.setTelefono(clienteDTO.getTelefono());


            Cliente guardado = clienteRepository.save(cliente);

            // Publicar evento
            eventPublisher.publicarClienteEditado(guardado);

            respuesta.llenarRespuestaExitosa("Cliente editado correctamente", guardado.toDTO());

        } catch (Exception ex) {
            respuesta.llenarConDatosDeException(ex);
        }

        return respuesta;
    }

    @Override
    @Transactional
    public RespuestaPorDefecto<ClienteDTO> eliminarCliente(Integer idCliente) {
        RespuestaPorDefecto<ClienteDTO> respuesta = new RespuestaPorDefecto<>();

        try {
            Cliente cliente = clienteRepository.findByIdPersonaAndEstado(idCliente.longValue(), true);

            if(cliente == null) {
                respuesta.setMensaje("El cliente no existe");
                return respuesta;
            }

            cliente.setEstado(false);
            ClienteDTO eliminado = cliente.toDTO();
            clienteRepository.save(cliente);

            // Publicar evento
            eventPublisher.publicarClienteEliminado(cliente);

            respuesta.llenarRespuestaExitosa("Cliente eliminado correctamente", eliminado);

        } catch (Exception ex) {
            respuesta.llenarConDatosDeException(ex);
        }

        return respuesta;
    }
}