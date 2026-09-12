package com.banco.ms_cuentas.controller;

import com.banco.ms_cuentas.dto.response.MovimientoCuentaDTO;
import com.banco.ms_cuentas.dto.request.MovimientoRequest;
import com.banco.ms_cuentas.dto.response.RespuestaPorDefecto;
import com.banco.ms_cuentas.services.MovimientoCuentaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@AllArgsConstructor
public class MovimientoCuentaController {

    private final MovimientoCuentaService movimientoCuentaService;

    @GetMapping("/cuenta/{idCuenta}")
    public ResponseEntity<RespuestaPorDefecto<List<MovimientoCuentaDTO>>> listar(
            @PathVariable Long idCuenta) {
        return ResponseEntity.ok(
                movimientoCuentaService.listarMovimientoCuenta(idCuenta));
    }

    @GetMapping("/{idMovimiento}")
    public ResponseEntity<RespuestaPorDefecto<MovimientoCuentaDTO>> obtener(
            @PathVariable Long idMovimiento) {
        return ResponseEntity.ok(
                movimientoCuentaService.obtenerMovimiento(idMovimiento));
    }

    @PostMapping
    public ResponseEntity<RespuestaPorDefecto<MovimientoCuentaDTO>> crear(
            @RequestBody MovimientoRequest dto) {
        return ResponseEntity.ok(movimientoCuentaService.crear(dto));
    }

    @PutMapping
    public ResponseEntity<RespuestaPorDefecto<MovimientoCuentaDTO>> editar(
            @RequestBody MovimientoRequest dto) {
        return ResponseEntity.ok(movimientoCuentaService.editar(dto));
    }

    @DeleteMapping("/{idMovimiento}")
    public ResponseEntity<RespuestaPorDefecto<MovimientoCuentaDTO>> eliminar(
            @PathVariable Long idMovimiento) {
        return ResponseEntity.ok(movimientoCuentaService.eliminarMovimiento(idMovimiento));
    }
}