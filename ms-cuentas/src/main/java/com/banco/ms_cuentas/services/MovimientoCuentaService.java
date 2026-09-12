package com.banco.ms_cuentas.services;

import com.banco.ms_cuentas.dto.response.MovimientoCuentaDTO;
import com.banco.ms_cuentas.dto.request.MovimientoRequest;
import com.banco.ms_cuentas.dto.response.RespuestaPorDefecto;

import java.util.List;

public interface MovimientoCuentaService {

    /**
     *  Servicio para obtener todos los movimiento de una cuenta
     */
    RespuestaPorDefecto<List<MovimientoCuentaDTO>> listarMovimientoCuenta(Long idCuenta);

    /**
     *  Servicio para obtener un movimiento
     */
    RespuestaPorDefecto<MovimientoCuentaDTO> obtenerMovimiento(Long idMovimiento);

    /**
     *  Servicio para crear un movimiento de una cuenta
     */
    RespuestaPorDefecto<MovimientoCuentaDTO> crear(MovimientoRequest movimientoCuentaDTO);

    /**
     *  Servicio para editar un movimiento de una cuenta
     */
    RespuestaPorDefecto<MovimientoCuentaDTO> editar(MovimientoRequest movimientoCuentaDTO);

    /**
     *  Servicio para eliminar un movimiento de una cuenta
     */
    RespuestaPorDefecto<MovimientoCuentaDTO> eliminarMovimiento(Long idMovimiento);
}
