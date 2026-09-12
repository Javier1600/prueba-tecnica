package com.banco.ms_clientes.dto;

import lombok.Data;

@Data
public class PersonaDTO {
    private Long idPersona;
    private String nombre;
    private String genero;
    private Integer edad;
    private String identificacion;
    private String direccion;
    private String telefono;
}
