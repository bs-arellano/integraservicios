package com.syntaxerror.seminario.model;

public enum EstadoTransaccion {
    ACTIVA("activa"),
    CANCELADA("cancelada"),
    COMPLETADA("completada");
    private String value;

    EstadoTransaccion(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
