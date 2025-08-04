package com.dinet.application.dto;

public record PedidoDTO(
        String numeroPedido,
        String clienteId,
        String fechaEntrega,
        String estado,
        String zonaEntrega,
        boolean requiereRefrigeracion
) {}