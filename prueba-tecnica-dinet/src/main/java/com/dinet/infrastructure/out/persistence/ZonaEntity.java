package com.dinet.infrastructure.out.persistence;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "zonas")
public class ZonaEntity {
    @Id
    private String codigo;

    private boolean soporteRefrigeracion;

    public ZonaEntity() {}

    public ZonaEntity(String codigo, boolean soporteRefrigeracion) {
        this.codigo = codigo;
        this.soporteRefrigeracion = soporteRefrigeracion;
    }
}
