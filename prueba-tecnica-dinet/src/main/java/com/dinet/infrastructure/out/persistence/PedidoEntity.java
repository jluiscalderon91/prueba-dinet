package com.dinet.infrastructure.out.persistence;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "pedidos")

public class PedidoEntity {
    @Id
    private String numeroPedido;

    @Column(nullable = false)
    private String clienteId;

    @Column(nullable = false)
    private LocalDate fechaEntrega;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado;

    @Column(nullable = false)
    private String zonaEntrega;

    @Column(nullable = false)
    private boolean requiereRefrigeracion;

    public enum Estado {
        PENDIENTE,
        CONFIRMADO,
        ENTREGADO
    }

    public PedidoEntity() {
    }

    public PedidoEntity(String numeroPedido, String clienteId, LocalDate fechaEntrega,
                        Estado estado, String zonaEntrega, boolean requiereRefrigeracion) {
        this.numeroPedido = numeroPedido;
        this.clienteId = clienteId;
        this.fechaEntrega = fechaEntrega;
        this.estado = estado;
        this.zonaEntrega = zonaEntrega;
        this.requiereRefrigeracion = requiereRefrigeracion;
    }
}