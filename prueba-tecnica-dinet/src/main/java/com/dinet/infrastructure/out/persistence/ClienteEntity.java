package com.dinet.infrastructure.out.persistence;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "cliente")
public class ClienteEntity {
    @Id
    private String id;

    private String nombre;

    public ClienteEntity() {}

    public ClienteEntity(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
}
