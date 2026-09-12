package com.banco.ms_cuentas.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CuentaDTO {
    private Long idCuenta;
    private String numeroCuenta;
    private String tipoCuenta;
    private BigDecimal  saldoActual;
    private BigDecimal  saldoInicial;
    private boolean estado;
    private ClienteDTO cliente;
}
