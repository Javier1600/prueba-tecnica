package com.banco.ms_clientes.dto;

import lombok.Data;

@Data
public class ClienteDTO extends PersonaDTO{
    private String password;
    private boolean estado;
}
