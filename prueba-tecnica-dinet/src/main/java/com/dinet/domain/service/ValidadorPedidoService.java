package com.dinet.domain.service;

import com.dinet.application.dto.PedidoDTO;
import com.dinet.domain.model.Zona;
import com.dinet.infrastructure.out.persistence.ClienteRepository;
import com.dinet.infrastructure.out.persistence.ZonaEntity;
import com.dinet.infrastructure.out.persistence.ZonaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ValidadorPedidoService {
    private final ZonaRepository zonaRepository;
    private final ClienteRepository clienteRepository;
    private final Set<String> estadosValidos = Set.of("PENDIENTE", "CONFIRMADO", "ENTREGADO");

    public ValidadorPedidoService(ZonaRepository zonaRepository, ClienteRepository clienteRepository) {
        this.zonaRepository = zonaRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<String> validar(
            PedidoDTO dto,
            Set<String> pedidosExistentes
    ) {
        List<String> errores = new ArrayList<>();

        if (dto.numeroPedido() == null || dto.numeroPedido().isBlank()) {
            errores.add("Número de pedido vacío");
        } else if (pedidosExistentes.contains(dto.numeroPedido())) {
            errores.add("Número de pedido duplicado");
        }

        if (!clienteRepository.existsById(dto.clienteId())) {
            errores.add("Cliente no encontrado");
        }

        try {
            LocalDate fecha = LocalDate.parse(dto.fechaEntrega());
            if (fecha.isBefore(LocalDate.now())) {
                errores.add("Fecha de entrega en el pasado");
            }
        } catch (Exception e) {
            errores.add("Fecha de entrega inválida");
        }

        if (dto.estado() == null || !estadosValidos.contains(dto.estado().toUpperCase())) {
            errores.add("Estado inválido");
        }

        ZonaEntity zona = zonaRepository.findById(dto.zonaEntrega()).orElse(null);
        if (zona == null) {
            errores.add("Zona inválida");
        } else if (dto.requiereRefrigeracion() && !zona.isSoporteRefrigeracion()) {
            errores.add("Zona no soporta refrigeración");
        }

        return errores;
    }
}
