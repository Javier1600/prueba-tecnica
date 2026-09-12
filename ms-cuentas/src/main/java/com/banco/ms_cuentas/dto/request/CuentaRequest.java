package com.banco.ms_cuentas.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CuentaRequest {
    private Long idCuenta;
    private String numeroCuenta;
    private String tipoCuenta;
    private BigDecimal saldoActual;
    private BigDecimal  saldoInicial;
    private boolean estado;
    private Long idCliente;
}
