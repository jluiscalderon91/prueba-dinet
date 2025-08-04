package com.dinet.domain.model;

import lombok.Getter;

public class Zona {
    @Getter
    private String codigo;
    private boolean soporteRefrigeracion;

    public Zona(String codigo, boolean soporteRefrigeracion) {
        this.codigo = codigo;
        this.soporteRefrigeracion = soporteRefrigeracion;
    }

    public boolean soporteRefrigeracion() {
        return soporteRefrigeracion;
    }
}
