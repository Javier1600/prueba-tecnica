package com.banco.ms_clientes.entities;

import com.banco.ms_clientes.dto.ClienteDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;

@Entity
@Data
@Table(name = "cliente")
@EqualsAndHashCode(callSuper = true)
@Comment("Tabla de clientes")
@PrimaryKeyJoinColumn(name = "cliente_id")
public class Cliente extends Persona{

    @Column(nullable = false)
    private Boolean estado = true;

    @Column(length = 100)
    private String password;

    public ClienteDTO toDTO(){
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setPassword(this.password);
        clienteDTO.setEstado(this.estado);

        clienteDTO.setIdPersona(super.getIdPersona());
        clienteDTO.setNombre(super.getNombre());
        clienteDTO.setGenero(super.getGenero());
        clienteDTO.setEdad(super.getEdad());
        clienteDTO.setIdentificacion(super.getIdentificacion());
        clienteDTO.setDireccion(super.getDireccion());
        clienteDTO.setTelefono(super.getTelefono());

        return clienteDTO;
    }
}
