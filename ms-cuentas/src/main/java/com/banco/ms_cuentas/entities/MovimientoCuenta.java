package com.banco.ms_cuentas.entities;

import com.banco.ms_cuentas.dto.response.MovimientoCuentaDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "movimiento")
@Comment("Tabla de movimientos en cuentas")
public class MovimientoCuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movimiento_id")
    private Long movimientoId;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(name = "tipo_movimiento", nullable = false, length = 20)
    private String tipoMovimiento;   // Depósito | Retiro

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_id", nullable = false)
    private Cuenta cuenta;

    @Column(nullable = false)
    private Boolean estado = true;

    public MovimientoCuentaDTO toDTO () {
        MovimientoCuentaDTO dto = new MovimientoCuentaDTO();
        dto.setMovimientoId(this.movimientoId);
        dto.setFecha(this.fecha);
        dto.setTipoMovimiento(this.tipoMovimiento);
        dto.setMonto(this.valor);
        dto.setSaldo(this.saldo);
        dto.setCuenta(this.cuenta != null ? this.cuenta.toDTO() : null);

        dto.setCuenta(this.cuenta.toDTO());

        return dto;
    }
}
