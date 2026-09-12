package com.banco.ms_cuentas.services;

import com.banco.ms_cuentas.dto.request.CuentaRequest;
import com.banco.ms_cuentas.dto.response.CuentaDTO;
import com.banco.ms_cuentas.dto.response.RespuestaPorDefecto;
import jakarta.validation.constraints.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CuentaService {

    /**
     * Servicio para obtener todas las cuentas activas
     */
    RespuestaPorDefecto<List<CuentaDTO>> obtenerCuentas();

    /**
     * Servicio para obtener todas las cuentas activas por cliente
     */
    RespuestaPorDefecto<List<CuentaDTO>> obtenerCuentasPorCliente(@NotNull Long idCliente);

    /**
     * Servicio para obtener un cuenta
     */
    RespuestaPorDefecto<CuentaDTO> obtenerCuenta(@NotNull Long idCuenta);

    /**
     * Servicio para crear una cuenta
     */
    RespuestaPorDefecto<CuentaDTO> crearCuenta(@NotNull CuentaRequest cuentaDTO);

    /**
     * Servicio para editar una cuenta
     */
    @Transactional
    RespuestaPorDefecto<CuentaDTO> editarCuenta(@NotNull CuentaRequest cuentaDTO);


    @Transactional
    RespuestaPorDefecto<CuentaDTO> eliminarCuenta(Long idCuenta);
}
