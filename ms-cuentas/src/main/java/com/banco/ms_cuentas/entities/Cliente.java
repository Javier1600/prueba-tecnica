package com.banco.ms_cuentas.entities;

import com.banco.ms_cuentas.dto.response.ClienteDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "cliente")
@Comment("Proyección local de clientes")
public class Cliente {

    @Id
    private Long clienteId;

    private String nombre;
    private String identificacion;
    private Boolean estado;

    private LocalDateTime fechaSincronizacion;

    public ClienteDTO toDTO() {
        ClienteDTO dto = new ClienteDTO();
        dto.setIdCliente(this.clienteId);
        dto.setNombre(this.nombre);
        dto.setIdentificacion(this.identificacion);
        dto.setEstado(this.estado);

        return dto;
    }

}