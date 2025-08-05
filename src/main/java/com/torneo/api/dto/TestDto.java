package com.torneo.api.dto;

import lombok.Data;

public class TestDto {
    private String nombre;

    public TestDto() {}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
