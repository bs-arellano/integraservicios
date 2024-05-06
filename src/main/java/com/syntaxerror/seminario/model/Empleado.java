package com.syntaxerror.seminario.model;

import jakarta.persistence.*;

@Entity
@Table(name = "\"Empleados\"")
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "empleado_id")
    private Long empleadoId;

    @Column(name = "unidad_id", nullable = false)
    private Long unidadId;

    // Getters and setters
    public Long getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(Long empleadoId) {
        this.empleadoId = empleadoId;
    }

    public Long getUnidadId() {
        return unidadId;
    }

    public void setUnidadId(Long unidadId) {
        this.unidadId = unidadId;
    }
}