package com.banco.ms_cuentas.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteEvent implements Serializable{
    Long clienteId;
    String nombre;
    String identificacion;
    Boolean estado;
}
