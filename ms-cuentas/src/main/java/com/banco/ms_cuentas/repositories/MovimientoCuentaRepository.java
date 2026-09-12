package com.banco.ms_cuentas.repositories;

import com.banco.ms_cuentas.entities.MovimientoCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovimientoCuentaRepository extends JpaRepository<MovimientoCuenta,Long> {

    List<MovimientoCuenta> findByCuentaCuentaIdAndEstadoOrderByMovimientoIdDesc(Long cuentaId, boolean estado);

    MovimientoCuenta findByMovimientoIdAndEstado(Long idMovimiento, boolean estado);

    @Query(
            nativeQuery = true,
            value = """
                SELECT m.* FROM movimiento m 
                JOIN cuenta c ON c.cuenta_id = m.cuenta_id
                WHERE c.estado = true AND m.estado = true
                AND date(m.fecha) >= date(:fechaDesde) and date(m.fecha) <= date(:fechaHasta)
                AND (:clienteId IS NULL OR c.cliente_id = :clienteId)
                ORDER BY c.cuenta_id, m.fecha desc
            """
    )
    List<MovimientoCuenta> obtenerPorRangoFechasAndCliente(
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta,
            @Param("clienteId") Long clienteId
    );
}
