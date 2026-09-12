package com.banco.ms_clientes.repositories;

import com.banco.ms_clientes.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    List<Cliente> findByEstado(boolean estado);

    Cliente findByIdPersonaAndEstado(Long idCliente, boolean estado);

    Cliente findByIdentificacionAndEstado(String identificacion, boolean estado);

    boolean existsByIdentificacion(String identificacion);

}
