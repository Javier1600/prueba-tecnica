package com.banco.ms_cuentas.services;

import com.banco.ms_cuentas.dto.response.ReporteDTO;
import com.banco.ms_cuentas.dto.response.RespuestaPorDefecto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReporteService {

    RespuestaPorDefecto<List<ReporteDTO>> generarReporte(
            Long clienteId, LocalDate fechaDesde, LocalDate fechaHasta
    );

}
