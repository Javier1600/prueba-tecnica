package com.banco.ms_cuentas.controller;

import com.banco.ms_cuentas.dto.response.ReporteDTO;
import com.banco.ms_cuentas.dto.response.RespuestaPorDefecto;
import com.banco.ms_cuentas.services.ReporteService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@AllArgsConstructor
public class ReportesController {

    private final ReporteService reporteService;

    @GetMapping
    public ResponseEntity<RespuestaPorDefecto<List<ReporteDTO>>> obtenerReporte(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(required = false) Long clienteId) {

        return ResponseEntity.ok(
                reporteService.generarReporte(clienteId, fechaDesde, fechaHasta));
    }

}
