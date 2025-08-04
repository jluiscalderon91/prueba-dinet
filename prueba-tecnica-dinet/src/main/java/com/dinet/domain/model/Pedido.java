package com.dinet.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class Pedido {

    private String numeroPedido;
    private String clienteId;
    private LocalDate fechaEntrega;
    private EstadoPedido estado;
    private String zonaEntrega;
    private boolean requiereRefrigeracion;

    public enum EstadoPedido {
        PENDIENTE, CONFIRMADO, ENTREGADO
    }

    public Pedido(String numeroPedido, String clienteId, LocalDate fechaEntrega,
                  EstadoPedido estado, String zonaEntrega, boolean requiereRefrigeracion) {
        this.numeroPedido = numeroPedido;
        this.clienteId = clienteId;
        this.fechaEntrega = fechaEntrega;
        this.estado = estado;
        this.zonaEntrega = zonaEntrega;
        this.requiereRefrigeracion = requiereRefrigeracion;
    }
}
