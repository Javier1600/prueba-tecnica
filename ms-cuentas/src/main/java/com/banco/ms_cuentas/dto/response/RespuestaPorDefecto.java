package com.banco.ms_cuentas.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RespuestaPorDefecto<T> {
    private String titulo = "Petición no exitosa";
    private String mensaje;
    private boolean exito = false;
    private T data;
    private Integer codigoEstado;

    /**
     * LLena los datos con una respuesta exito
     *
     * @param mensaje Mensaje final que se le envia al usuario.
     * @param data data final enviada al usuario.
     *
     * @return void.
     */
    public void llenarRespuestaExitosa(String mensaje, T data) {
        this.exito = true;
        this.codigoEstado = 200;
        this.titulo = "Petición realizada con éxito";
        this.mensaje = mensaje;
        this.data = data;
    }

    /**
     * Llena el objeto con los datos obtenidos en la exception
     *
     * @param ex Objeto Exception.
     *
     * @return void
     */
    public void llenarConDatosDeException(@NotNull Exception ex) {
        this.codigoEstado = 500;
        this.mensaje = "No se pudo realizar la operación debido a que ocurrido el siguiente error: " + ex.getMessage();
        this.titulo = "Petición fallida";
        this.exito = false;
    }

}
