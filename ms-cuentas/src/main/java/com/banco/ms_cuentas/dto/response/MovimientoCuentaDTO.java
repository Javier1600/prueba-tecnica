package com.banco.ms_cuentas.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MovimientoCuentaDTO {
    private Long movimientoId;
    private String tipoMovimiento;
    private BigDecimal monto;
    private LocalDateTime fecha;
    private BigDecimal saldo;
    private CuentaDTO cuenta;
}
