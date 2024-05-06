package com.syntaxerror.seminario.model;

public enum DiaSemana {
    LUNES("lunes"),
    MARTES("martes"),
    MIERCOLES("miercoles"),
    JUEVES("jueves"),
    VIERNES("viernes");

    private String value;

    DiaSemana(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}