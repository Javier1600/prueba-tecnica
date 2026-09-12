package com.banco.ms_cuentas.services;

import com.banco.ms_cuentas.dto.request.CuentaRequest;
import com.banco.ms_cuentas.dto.response.CuentaDTO;
import com.banco.ms_cuentas.dto.response.RespuestaPorDefecto;
import com.banco.ms_cuentas.entities.Cliente;
import com.banco.ms_cuentas.entities.Cuenta;
import com.banco.ms_cuentas.entities.MovimientoCuenta;
import com.banco.ms_cuentas.repositories.ClienteRepository;
import com.banco.ms_cuentas.repositories.CuentaRepository;
import com.banco.ms_cuentas.repositories.MovimientoCuentaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final MovimientoCuentaRepository movimientoCuentaRepository;
    private final ClienteRepository clienteRepository;

    @Override
    @Transactional(readOnly = true)
    public RespuestaPorDefecto<List<CuentaDTO>> obtenerCuentas() {
        RespuestaPorDefecto<List<CuentaDTO>> respuesta = new RespuestaPorDefecto<>();

        try {
            List<CuentaDTO> cuentas = cuentaRepository.findByEstadoOrderByCuentaIdDesc(true)
                    .stream()
                    .map(Cuenta::toDTO)
                    .toList();

            if(cuentas.isEmpty()){
                respuesta.setMensaje("No se encontraron cuentas activas para mostrar");
                return respuesta;
            }

            respuesta.llenarRespuestaExitosa("Cuentas obtenidas correctamente", cuentas);
        } catch (Exception ex) {
            respuesta.llenarConDatosDeException(ex);
        }

        return respuesta;
    }

    @Override
    @Transactional(readOnly = true)
    public RespuestaPorDefecto<List<CuentaDTO>> obtenerCuentasPorCliente(Long idCliente) {
        RespuestaPorDefecto<List<CuentaDTO>> respuesta = new RespuestaPorDefecto<>();

        try {
            // Validar que el cliente exista localmente
            if (!clienteRepository.existsByClienteIdAndEstado(idCliente, true)) {
                respuesta.setMensaje(
                        "Cliente no encontrado o inactivo con id: " + idCliente
                );
                return respuesta;
            }

            Cliente cliente = clienteRepository.findByClienteIdAndEstado(idCliente, true);

            List<CuentaDTO> cuentas = cuentaRepository
                    .findByClienteClienteIdAndEstadoOrderByCuentaIdDesc(idCliente, true)
                    .stream()
                    .map(Cuenta::toDTO)
                    .toList();

            if(cuentas.isEmpty()){
                respuesta.setMensaje("No se encontraron cuentas activas para mostrar para el cliente " + cliente.getNombre());
                return respuesta;
            }

            respuesta.llenarRespuestaExitosa("Cuentas del cliente obtenidas correctamente", cuentas);
        } catch (Exception ex) {
            respuesta.llenarConDatosDeException(ex);
        }

        return respuesta;
    }

    @Override
    @Transactional(readOnly = true)
    public RespuestaPorDefecto<CuentaDTO> obtenerCuenta(Long idCuenta) {
        RespuestaPorDefecto<CuentaDTO> respuesta = new RespuestaPorDefecto<>();

        try {
            Cuenta cuenta = cuentaRepository.findByCuentaIdAndEstado(idCuenta, true);

            if(cuenta == null){
                respuesta.setMensaje("No se encontraron cuenta con id: " + idCuenta);
                return respuesta;
            }

            respuesta.llenarRespuestaExitosa("Cuenta encontrada", cuenta.toDTO());
        } catch (Exception ex) {
            respuesta.llenarConDatosDeException(ex);
        }

        return respuesta;
    }

    @Override
    @Transactional
    public RespuestaPorDefecto<CuentaDTO> crearCuenta(CuentaRequest cuentaDTO) {
        RespuestaPorDefecto<CuentaDTO> respuesta = new RespuestaPorDefecto<>();

        try {
            // Validar que el cliente exista localmente
            if (cuentaDTO.getIdCliente() == null) {
                respuesta.setMensaje("El cliente es obligatorio para crear una cuenta");
            }

            Long idCliente = cuentaDTO.getIdCliente();

            Cliente cliente = clienteRepository.findByClienteIdAndEstado(idCliente, true);

            if(cliente == null){
                respuesta.setMensaje("No se encontraron cliente con id: " + idCliente);
                return respuesta;
            }

            // Validar que el número de cuenta no exista
            if (cuentaRepository.findByNumeroCuentaAndEstado(cuentaDTO.getNumeroCuenta(), true) != null) {
                respuesta.setMensaje(
                        "Ya existe una cuenta con el número: " + cuentaDTO.getNumeroCuenta()
                );
                return respuesta;
            }

            Cuenta cuenta = new Cuenta();
            cuenta.setNumeroCuenta(cuentaDTO.getNumeroCuenta());
            cuenta.setTipoCuenta(cuentaDTO.getTipoCuenta());
            cuenta.setSaldoInicial(cuentaDTO.getSaldoInicial() != null
                    ? cuentaDTO.getSaldoInicial()
                    : BigDecimal.ZERO);
            cuenta.setSaldoDisponible(cuenta.getSaldoInicial());
            cuenta.setEstado(cuentaDTO.isEstado());
            cuenta.setCliente(cliente);

            Cuenta guardada = cuentaRepository.save(cuenta);

            respuesta.llenarRespuestaExitosa("Cuenta creada correctamente", guardada.toDTO());

        } catch (Exception ex) {
            respuesta.llenarConDatosDeException(ex);
        }

        return respuesta;
    }

    @Override
    @Transactional
    public RespuestaPorDefecto<CuentaDTO> editarCuenta(CuentaRequest cuentaDTO) {
        RespuestaPorDefecto<CuentaDTO> respuesta = new RespuestaPorDefecto<>();

        try {
            if (cuentaDTO.getIdCuenta() == null) {
                respuesta.setMensaje("El id de la cuenta es obligatorio para editar");
                return respuesta;
            }

            // Buscar cuenta existente
            Cuenta cuenta = cuentaRepository.findByCuentaIdAndEstado(cuentaDTO.getIdCuenta(), true);

            if(cuenta == null){
                respuesta.setMensaje("No se ha encontrado la cuenta con id: " + cuentaDTO.getIdCuenta());
                return respuesta;
            }

            cuenta.setTipoCuenta(cuentaDTO.getTipoCuenta());
            cuenta.setEstado(cuentaDTO.isEstado());

            Cuenta actualizada = cuentaRepository.save(cuenta);

            respuesta.llenarRespuestaExitosa("Cuenta actualizada correctamente", actualizada.toDTO());

        } catch (Exception ex) {
            respuesta.llenarConDatosDeException(ex);
        }

        return respuesta;
    }

    @Transactional
    @Override
    public RespuestaPorDefecto<CuentaDTO> eliminarCuenta(Long idCuenta) {
        RespuestaPorDefecto<CuentaDTO> respuesta = new RespuestaPorDefecto<>();

        try {
            Cuenta cuenta = cuentaRepository.findByCuentaIdAndEstado(idCuenta, true);

            if(cuenta == null){
                respuesta.setMensaje("No se encontro la cuenta con id: " + idCuenta);
                return respuesta;
            }

            cuenta.setEstado(false);
            Cuenta eliminada = cuentaRepository.save(cuenta);

            List<MovimientoCuenta> movimientos = movimientoCuentaRepository
                    .findByCuentaCuentaIdAndEstadoOrderByMovimientoIdDesc(
                            idCuenta, true
                    );

            for(MovimientoCuenta movimiento : movimientos){
                movimiento.setEstado(false);
            }

            movimientoCuentaRepository.saveAll(movimientos);

            respuesta.llenarRespuestaExitosa("Cuenta eliminada correctamente", eliminada.toDTO());

        } catch (Exception ex) {
            respuesta.llenarConDatosDeException(ex);
        }

        return respuesta;
    }
}