package com.banco.ms_cuentas.repositories;

import com.banco.ms_cuentas.entities.Cliente;
import com.banco.ms_cuentas.entities.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    List<Cuenta> findByEstadoOrderByCuentaIdDesc(boolean estado);

    List<Cuenta> findByClienteClienteIdAndEstadoOrderByCuentaIdDesc(Long clienteId, boolean estado);

    Cuenta findByCuentaIdAndEstado(Long cuentaId, boolean estado);

    Cuenta findByNumeroCuentaAndEstado(String numeroCuenta, boolean estado);

}
