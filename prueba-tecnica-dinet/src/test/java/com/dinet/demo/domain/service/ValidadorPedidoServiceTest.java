package com.dinet.demo.domain.service;

import com.dinet.application.dto.PedidoDTO;
import com.dinet.domain.model.Zona;
import com.dinet.domain.service.ValidadorPedidoService;
import com.dinet.infrastructure.out.persistence.ClienteRepository;
import com.dinet.infrastructure.out.persistence.ZonaEntity;
import com.dinet.infrastructure.out.persistence.ZonaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ValidadorPedidoServiceTest {
    private ZonaRepository zonaRepository;
    private ClienteRepository clienteRepository;
    private ValidadorPedidoService validador;

    @BeforeEach
    void setUp() {
        zonaRepository = mock(ZonaRepository.class);
        clienteRepository = mock(ClienteRepository.class);
        validador = new ValidadorPedidoService(zonaRepository, clienteRepository);
    }

    @Test
    void debeValidarPedidoCorrectamente() {
        PedidoDTO dto = new PedidoDTO("P001", "CLI-123", LocalDate.now().plusDays(1).toString(), "PENDIENTE", "ZONA1", true);

        when(clienteRepository.existsById("CLI-123")).thenReturn(true);
        when(zonaRepository.findById("ZONA1")).thenReturn(Optional.of(new ZonaEntity("ZONA1", true)));

        List<String> errores = validador.validar(dto, Set.of());

        assertTrue(errores.isEmpty());
    }

    @Test
    void debeDetectarClienteNoEncontrado() {
        PedidoDTO dto = new PedidoDTO("P002", "CLI-999", LocalDate.now().plusDays(1).toString(), "PENDIENTE", "ZONA1", false);

        when(clienteRepository.existsById("CLI-999")).thenReturn(false);
        when(zonaRepository.findById("ZONA1")).thenReturn(Optional.of(new ZonaEntity("ZONA1", true)));

        List<String> errores = validador.validar(dto, Set.of());

        assertTrue(errores.contains("Cliente no encontrado"));
    }

    @Test
    void debeDetectarZonaInvalida() {
        PedidoDTO dto = new PedidoDTO("P003", "CLI-123", LocalDate.now().plusDays(1).toString(), "PENDIENTE", "ZONA-X", false);

        when(clienteRepository.existsById("CLI-123")).thenReturn(true);
        when(zonaRepository.findById("ZONA-X")).thenReturn(Optional.empty());

        List<String> errores = validador.validar(dto, Set.of());

        assertTrue(errores.contains("Zona inválida"));
    }

    @Test
    void debeDetectarZonaSinRefrigeracion() {
        PedidoDTO dto = new PedidoDTO("P004", "CLI-123", LocalDate.now().plusDays(1).toString(), "PENDIENTE", "ZONA2", true);

        when(clienteRepository.existsById("CLI-123")).thenReturn(true);
        when(zonaRepository.findById("ZONA2")).thenReturn(Optional.of(new ZonaEntity("ZONA2", false)));

        List<String> errores = validador.validar(dto, Set.of());

        assertTrue(errores.contains("Zona no soporta refrigeración"));
    }

    @Test
    void debeDetectarEstadoInvalido() {
        PedidoDTO dto = new PedidoDTO("P005", "CLI-123", LocalDate.now().plusDays(1).toString(), "EN_PROCESO", "ZONA1", false);

        when(clienteRepository.existsById("CLI-123")).thenReturn(true);
        when(zonaRepository.findById("ZONA1")).thenReturn(Optional.of(new ZonaEntity("ZONA1", true)));

        List<String> errores = validador.validar(dto, Set.of());

        assertTrue(errores.contains("Estado inválido"));
    }

    @Test
    void debeDetectarFechaEntregaInvalida() {
        PedidoDTO dto = new PedidoDTO("P006", "CLI-123", "fecha-mal", "PENDIENTE", "ZONA1", false);

        when(clienteRepository.existsById("CLI-123")).thenReturn(true);
        when(zonaRepository.findById("ZONA1")).thenReturn(Optional.of(new ZonaEntity("ZONA1", true)));

        List<String> errores = validador.validar(dto, Set.of());

        assertTrue(errores.contains("Fecha de entrega inválida"));
    }

    @Test
    void debeDetectarFechaEntregaPasada() {
        PedidoDTO dto = new PedidoDTO("P007", "CLI-123", LocalDate.now().minusDays(1).toString(), "PENDIENTE", "ZONA1", false);

        when(clienteRepository.existsById("CLI-123")).thenReturn(true);
        when(zonaRepository.findById("ZONA1")).thenReturn(Optional.of(new ZonaEntity("ZONA1", true)));

        List<String> errores = validador.validar(dto, Set.of());

        assertTrue(errores.contains("Fecha de entrega en el pasado"));
    }

    @Test
    void debeDetectarPedidoDuplicado() {
        PedidoDTO dto = new PedidoDTO("P008", "CLI-123", LocalDate.now().plusDays(1).toString(), "PENDIENTE", "ZONA1", false);

        when(clienteRepository.existsById("CLI-123")).thenReturn(true);
        when(zonaRepository.findById("ZONA1")).thenReturn(Optional.of(new ZonaEntity("ZONA1", true)));

        List<String> errores = validador.validar(dto, Set.of("P008"));

        assertTrue(errores.contains("Número de pedido duplicado"));
    }
}
