package com.banco.ms_cuentas.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MovimientoRequest {
    private Long movimientoId;
    private String tipoMovimiento;
    private BigDecimal monto;
    private LocalDateTime fecha;
    private BigDecimal saldo;
    private Long cuentaId;
}
