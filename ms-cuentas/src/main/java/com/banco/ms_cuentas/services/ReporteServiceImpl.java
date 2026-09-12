package com.banco.ms_cuentas.services;

import com.banco.ms_cuentas.dto.response.ReporteDTO;
import com.banco.ms_cuentas.dto.response.RespuestaPorDefecto;
import com.banco.ms_cuentas.entities.Cliente;
import com.banco.ms_cuentas.entities.Cuenta;
import com.banco.ms_cuentas.entities.MovimientoCuenta;
import com.banco.ms_cuentas.repositories.CuentaRepository;
import com.banco.ms_cuentas.repositories.MovimientoCuentaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ReporteServiceImpl implements ReporteService{
    private final MovimientoCuentaRepository movimientoCuentaRepository;


    @Override
    public RespuestaPorDefecto<List<ReporteDTO>> generarReporte(
            Long clienteId, LocalDate fechaDesde, LocalDate fechaHasta
    ) {
        RespuestaPorDefecto<List<ReporteDTO>> respuesta = new RespuestaPorDefecto<>();

        try {
            if(fechaDesde.isAfter(fechaHasta)){
                respuesta.setMensaje("Proporcione un rango de fechas válido");
                return  respuesta;
            }

            List<MovimientoCuenta> movimientos = movimientoCuentaRepository
                    .obtenerPorRangoFechasAndCliente(
                            fechaDesde, fechaHasta, clienteId
                    );

            if(movimientos.isEmpty()){
                respuesta.setMensaje("No existen movimientos en el rango proporcionado");
                return  respuesta;
            }

            List<ReporteDTO> response = new ArrayList<>();

            for(MovimientoCuenta movimiento : movimientos){
                Cuenta cuenta = movimiento.getCuenta();
                Cliente cliente = cuenta.getCliente();
                ReporteDTO reporteDTO = new ReporteDTO();
                reporteDTO.setFecha(movimiento.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                reporteDTO.setCliente(cliente.getNombre());
                reporteDTO.setNumeroCuenta(cuenta.getNumeroCuenta());
                reporteDTO.setTipoCuenta(cuenta.getTipoCuenta());
                BigDecimal saldoAntesDeMovimiento;
                if(movimiento.getValor().compareTo(BigDecimal.ZERO) < 0){
                    saldoAntesDeMovimiento = movimiento.getSaldo().add(movimiento.getValor().abs());
                } else {
                    saldoAntesDeMovimiento = movimiento.getSaldo().subtract(movimiento.getValor().abs());
                }
                reporteDTO.setEstado(movimiento.getEstado());
                reporteDTO.setSaldoInicial(saldoAntesDeMovimiento);
                reporteDTO.setMovimiento(movimiento.getValor());
                reporteDTO.setSaldoDisponible(movimiento.getSaldo());

                response.add(reporteDTO);
            }

            respuesta.llenarRespuestaExitosa("Se obtuvo el reporte de forma correcta", response);

        } catch (Exception e) {
            respuesta.llenarConDatosDeException(e);
        }

        return respuesta;
    }
}
