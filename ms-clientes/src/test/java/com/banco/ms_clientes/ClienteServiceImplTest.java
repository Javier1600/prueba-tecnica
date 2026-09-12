package com.banco.ms_clientes;

import com.banco.ms_clientes.dto.ClienteDTO;
import com.banco.ms_clientes.dto.response.RespuestaPorDefecto;
import com.banco.ms_clientes.entities.Cliente;
import com.banco.ms_clientes.messaging.ClienteEventPublisher;
import com.banco.ms_clientes.repositories.ClienteRepository;
import com.banco.ms_clientes.services.ClienteService;
import com.banco.ms_clientes.services.ClienteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Prueba unitaria - ClienteServiceImpl")
public class ClienteServiceImplTest {
    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteEventPublisher clienteEventPublisher;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente clienteEntity;
    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        clienteDTO = new ClienteDTO();
        clienteDTO.setNombre("Jose Lema");
        clienteDTO.setGenero("M");
        clienteDTO.setEdad(30);
        clienteDTO.setIdentificacion("abcd123");
        clienteDTO.setDireccion("Otavalo sn y principal");
        clienteDTO.setTelefono("098254785");
        clienteDTO.setPassword("1234");
        clienteDTO.setEstado(true);

        // Entidad que simula lo que devuelve el repositorio
        clienteEntity = new Cliente();
        clienteEntity.setIdPersona(1L);
        clienteEntity.setNombre("Jose Lema");
        clienteEntity.setGenero("M");
        clienteEntity.setEdad(30);
        clienteEntity.setIdentificacion("abcd123");
        clienteEntity.setDireccion("Otavalo sn y principal");
        clienteEntity.setTelefono("098254785");
        clienteEntity.setPassword("1234");
        clienteEntity.setEstado(true);
    }

    @Test
    @DisplayName("crearCliente - crear y publicar el evento")
    void crearCliente() {
        // GIVEN
        when(clienteRepository.findByIdentificacionAndEstado("abcd123", true))
                .thenReturn(null);

        when(clienteRepository.save(any(Cliente.class)))
                .thenReturn(clienteEntity);

        RespuestaPorDefecto<ClienteDTO> respuesta = clienteService.crearCliente(clienteDTO);

        assertNotNull(respuesta);
        assertTrue(respuesta.isExito(), "Mensaje de error: " + respuesta.getMensaje());
        assertEquals(200, respuesta.getCodigoEstado());
        assertNotNull(respuesta.getData());
        assertEquals("Jose Lema", respuesta.getData().getNombre());
        assertEquals("abcd123", respuesta.getData().getIdentificacion());
        assertTrue(respuesta.getData().isEstado());

        verify(clienteRepository, times(1))
                .findByIdentificacionAndEstado("abcd123", true);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
        verify(clienteEventPublisher, times(1)).publicarClienteCreado(any(Cliente.class));
    }
}
