package com.banco.ms_cuentas.services;

import com.banco.ms_cuentas.dto.response.MovimientoCuentaDTO;
import com.banco.ms_cuentas.dto.request.MovimientoRequest;
import com.banco.ms_cuentas.dto.response.RespuestaPorDefecto;
import com.banco.ms_cuentas.entities.Cuenta;
import com.banco.ms_cuentas.entities.MovimientoCuenta;
import com.banco.ms_cuentas.repositories.CuentaRepository;
import com.banco.ms_cuentas.repositories.MovimientoCuentaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class MovimientoCuentaServiceImpl implements MovimientoCuentaService {

    private static final String TIPO_DEPOSITO = "Deposito";
    private static final String TIPO_RETIRO = "Retiro";

    private final CuentaRepository cuentaRepository;
    private final MovimientoCuentaRepository movimientoCuentaRepository;

    @Override
    @Transactional(readOnly = true)
    public RespuestaPorDefecto<List<MovimientoCuentaDTO>> listarMovimientoCuenta(Long idCuenta) {
        RespuestaPorDefecto<List<MovimientoCuentaDTO>> respuesta = new RespuestaPorDefecto<>();

        try {
            // Validar que la cuenta exista
            if (!cuentaRepository.existsById(idCuenta)) {
                respuesta.setMensaje(
                        "No se ha encontrado la cuenta con id: " + idCuenta
                );
                return respuesta;
            }

            List<MovimientoCuentaDTO> movimientos = movimientoCuentaRepository
                    .findByCuentaCuentaIdAndEstadoOrderByMovimientoIdDesc(idCuenta, true)
                    .stream()
                    .map(MovimientoCuenta::toDTO)
                    .toList();

            if(movimientos.isEmpty()){
                respuesta.setMensaje("No se ha encontrado movimiento con id: " + idCuenta);
                return respuesta;
            }

            respuesta.llenarRespuestaExitosa(
                    "Movimientos obtenidos correctamente", movimientos);

        } catch (Exception ex) {
            respuesta.llenarConDatosDeException(ex);
        }

        return respuesta;
    }

    @Override
    @Transactional(readOnly = true)
    public RespuestaPorDefecto<MovimientoCuentaDTO> obtenerMovimiento(Long idMovimiento) {
        RespuestaPorDefecto<MovimientoCuentaDTO> respuesta = new RespuestaPorDefecto<>();

        try {
            MovimientoCuenta movimiento = movimientoCuentaRepository
                    .findByMovimientoIdAndEstado(idMovimiento, true);

            if (movimiento == null) {
                respuesta.setMensaje("No existe el movimiento con el id: " + idMovimiento);
                return respuesta;
            }

            respuesta.llenarRespuestaExitosa("Movimiento encontrado", movimiento.toDTO());

        } catch (Exception ex) {
            respuesta.llenarConDatosDeException(ex);
        }

        return respuesta;
    }

    @Override
    @Transactional
    public RespuestaPorDefecto<MovimientoCuentaDTO> crear(MovimientoRequest dto) {
        RespuestaPorDefecto<MovimientoCuentaDTO> respuesta = new RespuestaPorDefecto<>();

        try {
            // Validar que se haya recibido la cuenta
            if (dto.getCuentaId() == null) {
                respuesta.setMensaje("La cuenta es obligatoria");
                return respuesta;
            }

            if (dto.getMonto() == null || dto.getMonto().compareTo(BigDecimal.ZERO) == 0) {
                respuesta.setMensaje("El valor del movimiento no puede ser cero");
                return respuesta;
            }

            Long idCuenta = dto.getCuentaId();

            // Buscar la cuenta
            Cuenta cuenta = cuentaRepository.findByCuentaIdAndEstado(idCuenta, true);

            if (cuenta == null) {
                respuesta.setMensaje(
                        "Cuenta no encontrada o inactiva con id: " + idCuenta
                );
                return respuesta;
            }

            // Calcular nuevo saldo
            BigDecimal nuevoSaldo = cuenta.getSaldoDisponible().add(dto.getMonto());

            //  Validar saldo disponible
            if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
                respuesta.setMensaje("El saldo después de la transacción no puede ser negativo");
                return respuesta;
            }

            // Determino tipo de movimiento según el signo
            String tipo = dto.getMonto().compareTo(BigDecimal.ZERO) > 0
                    ? TIPO_DEPOSITO
                    : TIPO_RETIRO;

            MovimientoCuenta movimiento = new MovimientoCuenta();
            movimiento.setFecha(LocalDateTime.now());
            movimiento.setTipoMovimiento(tipo);
            movimiento.setValor(dto.getMonto());
            movimiento.setSaldo(nuevoSaldo);
            movimiento.setCuenta(cuenta);

            // Actualizo el saldo de la cuenta
            cuenta.setSaldoDisponible(nuevoSaldo);
            cuentaRepository.save(cuenta);

            MovimientoCuenta guardado = movimientoCuentaRepository.save(movimiento);

            respuesta.llenarRespuestaExitosa(
                    "Movimiento registrado correctamente", guardado.toDTO());

        } catch (Exception ex) {
            respuesta.llenarConDatosDeException(ex);
        }

        return respuesta;
    }

    @Override
    @Transactional
    public RespuestaPorDefecto<MovimientoCuentaDTO> editar(MovimientoRequest dto) {
        RespuestaPorDefecto<MovimientoCuentaDTO> respuesta = new RespuestaPorDefecto<>();

        try {
            if (dto.getMovimientoId() == null) {
                respuesta.setMensaje("El id del movimiento es obligatorio para editar");
                return respuesta;
            }

            MovimientoCuenta movimiento = movimientoCuentaRepository
                    .findByMovimientoIdAndEstado(dto.getMovimientoId(), true);

            if (movimiento == null) {
                respuesta.setMensaje("No se ha encontrado el movimiento con id: " + dto.getMovimientoId());
                return respuesta;
            }

            // Solo se permite editar el tipo (por ejemplo, corregir de Depósito a Retiro)
            // o el valor. No se recalcula el saldo para no descuadrar
            // el histórico.
            if (dto.getTipoMovimiento() != null) {
                movimiento.setTipoMovimiento(dto.getTipoMovimiento());
            }

            MovimientoCuenta actualizado = movimientoCuentaRepository.save(movimiento);

            respuesta.llenarRespuestaExitosa(
                    "Movimiento actualizado correctamente", actualizado.toDTO());

        } catch (Exception ex) {
            respuesta.llenarConDatosDeException(ex);
        }

        return respuesta;
    }

    @Override
    @Transactional
    public RespuestaPorDefecto<MovimientoCuentaDTO> eliminarMovimiento(Long idMovimiento) {
        RespuestaPorDefecto<MovimientoCuentaDTO> respuesta = new RespuestaPorDefecto<>();

        try {
            if (idMovimiento == null) {
                respuesta.setMensaje("El id del movimiento es obligatorio para eliminar");
                return respuesta;
            }

            MovimientoCuenta movimiento = movimientoCuentaRepository
                    .findByMovimientoIdAndEstado(idMovimiento, true);

            if (movimiento == null) {
                respuesta.setMensaje("Movimiento no encontrado");
                return respuesta;
            }

            MovimientoCuentaDTO eliminado = movimiento.toDTO();

            // Revertir el efecto del movimiento en el saldo de la cuenta
            Cuenta cuenta = movimiento.getCuenta();
            BigDecimal saldoRevertido = cuenta.getSaldoDisponible().subtract(movimiento.getValor());

            if (saldoRevertido.compareTo(BigDecimal.ZERO) < 0) {
                respuesta.setMensaje(
                        "No se puede eliminar el movimiento: el saldo quedaría negativo"
                );
                return respuesta;
            }

            cuenta.setSaldoDisponible(saldoRevertido);
            cuentaRepository.save(cuenta);

            movimiento.setEstado(false);
            movimientoCuentaRepository.save(movimiento);

            respuesta.llenarRespuestaExitosa("Movimiento eliminado correctamente", eliminado);

        } catch (Exception ex) {
            respuesta.llenarConDatosDeException(ex);
        }

        return respuesta;
    }
}