package com.banco.ms_cuentas.repositories;

import com.banco.ms_cuentas.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Cliente findByClienteIdAndEstado(Long clienteId, boolean estado);

    boolean existsByClienteIdAndEstado(Long clienteId, Boolean estado);
}
