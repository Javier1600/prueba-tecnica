package com.banco.ms_cuentas.dto.response;

import lombok.Data;

@Data
public class ClienteDTO {
    private Long idCliente;
    private String nombre;
    private String identificacion;
    private boolean estado;
}
