
package com.banco.ms_cuentas.controller;

import com.banco.ms_cuentas.dto.request.CuentaRequest;
import com.banco.ms_cuentas.dto.response.CuentaDTO;
import com.banco.ms_cuentas.dto.response.RespuestaPorDefecto;
import com.banco.ms_cuentas.services.CuentaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/cuentas")
@AllArgsConstructor
public class CuentaController {

    private final CuentaService cuentaService;

    @GetMapping
    public ResponseEntity<RespuestaPorDefecto<List<CuentaDTO>>> obtenerCuentas() {
        return ResponseEntity.ok(cuentaService.obtenerCuentas());
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<RespuestaPorDefecto<List<CuentaDTO>>> obtenerCuentasPorCliente(
            @PathVariable Long idCliente) {
        return ResponseEntity.ok(cuentaService.obtenerCuentasPorCliente(idCliente));
    }

    @GetMapping("/{idCuenta}")
    public ResponseEntity<RespuestaPorDefecto<CuentaDTO>> obtenerCuenta(
            @PathVariable Long idCuenta) {
        return ResponseEntity.ok(cuentaService.obtenerCuenta(idCuenta));
    }

    @PostMapping
    public ResponseEntity<RespuestaPorDefecto<CuentaDTO>> crearCuenta(
            @RequestBody CuentaRequest cuentaDTO) {
        return ResponseEntity.ok(cuentaService.crearCuenta(cuentaDTO));
    }

    @PutMapping
    public ResponseEntity<RespuestaPorDefecto<CuentaDTO>> editarCuenta(
            @RequestBody CuentaRequest cuentaDTO) {
        return ResponseEntity.ok(cuentaService.editarCuenta(cuentaDTO));
    }

    @DeleteMapping("/{idCuenta}")
    public ResponseEntity<RespuestaPorDefecto<CuentaDTO>> eliminarCuenta(
            @PathVariable Long idCuenta) {
        return ResponseEntity.ok(cuentaService.eliminarCuenta(idCuenta));
    }
}

