package com.banco.ms_cuentas.entities;

import com.banco.ms_cuentas.dto.response.CuentaDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "cuenta")
@Comment("Tabla de cuentas")
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cuenta_id")
    private Long cuentaId;

    @Column(name = "numero_cuenta", nullable = false, unique = true, length = 20)
    private String numeroCuenta;

    @Column(name = "tipo_cuenta", nullable = false, length = 20)
    private String tipoCuenta;   // "Ahorro" | "Corriente"

    @Column(name = "saldo_inicial", nullable = false, precision = 15, scale = 2)
    private BigDecimal  saldoInicial;

    @Column(name = "saldo_disponible", nullable = false, precision = 15, scale = 2)
    private BigDecimal  saldoDisponible;

    @Column(nullable = false)
    private Boolean estado = true;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;   // referencia al cliente de MS1 (sin FK física entre micros)

    public CuentaDTO toDTO() {
        CuentaDTO cuentaDTO = new CuentaDTO();
        cuentaDTO.setIdCuenta(this.cuentaId);
        cuentaDTO.setNumeroCuenta(this.numeroCuenta);
        cuentaDTO.setTipoCuenta(this.tipoCuenta);
        cuentaDTO.setSaldoActual(this.saldoDisponible);
        cuentaDTO.setSaldoInicial(this.saldoInicial);
        cuentaDTO.setEstado(this.estado);
        cuentaDTO.setCliente(this.cliente.toDTO());

        return cuentaDTO;
    }
}
