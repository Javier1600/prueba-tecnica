package com.banco.ms_clientes.controller;


import com.banco.ms_clientes.dto.ClienteDTO;
import com.banco.ms_clientes.dto.response.RespuestaPorDefecto;
import com.banco.ms_clientes.services.ClienteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@AllArgsConstructor
public class ClienteController {
    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<RespuestaPorDefecto<List<ClienteDTO>>> obtenerClientes() {
        return ResponseEntity.ok(clienteService.obtenerClientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaPorDefecto<ClienteDTO>> obtenerCliente(
            @PathVariable Integer id) {
        return ResponseEntity.ok(clienteService.obtenerCliente(id));
    }

    @PostMapping
    public ResponseEntity<RespuestaPorDefecto<ClienteDTO>> crearCliente(
            @RequestBody ClienteDTO clienteDTO) {
        RespuestaPorDefecto<ClienteDTO> respuesta = clienteService.crearCliente(clienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping
    public ResponseEntity<RespuestaPorDefecto<ClienteDTO>> editarCliente(
            @RequestBody ClienteDTO clienteDTO) {
        RespuestaPorDefecto<ClienteDTO> respuesta = clienteService.editarCliente(clienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaPorDefecto<ClienteDTO>> eliminarCliente(
            @PathVariable Integer id) {
        return ResponseEntity.ok(clienteService.eliminarCliente(id));
    }
}
