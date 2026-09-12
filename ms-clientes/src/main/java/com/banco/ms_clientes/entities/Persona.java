package com.banco.ms_clientes.entities;

import com.banco.ms_clientes.dto.ClienteDTO;
import com.banco.ms_clientes.dto.PersonaDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Comment;

@Entity
@Data
@Table(name = "persona")
@Comment("Tabla de personas")
@Inheritance(strategy = InheritanceType.JOINED)
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "persona_id")
    private Long idPersona;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 20)
    private String genero;

    @Column
    private Integer edad;

    @Column(nullable = false, length = 20)
    private String identificacion;

    @Column(length = 200)
    private String direccion;

    @Column(length = 20)
    private String telefono;

    public PersonaDTO toDTO(){
        PersonaDTO dto = new PersonaDTO();

        dto.setIdPersona(this.idPersona);
        dto.setNombre(this.nombre);
        dto.setGenero(this.genero);
        dto.setEdad(this.edad);
        dto.setIdentificacion(this.identificacion);
        dto.setDireccion(this.direccion);
        dto.setTelefono(this.telefono);

        return dto;
    }
}
