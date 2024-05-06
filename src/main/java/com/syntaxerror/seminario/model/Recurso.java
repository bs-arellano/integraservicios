package com.syntaxerror.seminario.model;

import jakarta.persistence.*;

@Entity
@Table(name = "\"Recursos\"")
public class Recurso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recurso_id")
    private Long recursoId;

    @Column(name = "unidad_id", nullable = false)
    private Integer unidadId;

    @Column(name = "tipo_recurso_id", nullable = false)
    private Integer tipoRecursoId;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", nullable = false, length = 255)
    private String descripcion;

    // Getters and setters
    public Long getRecursoId() {
        return recursoId;
    }

    public void setRecursoId(Long recursoId) {
        this.recursoId = recursoId;
    }

    public Integer getUnidadId() {
        return unidadId;
    }

    public void setUnidadId(Integer unidadId) {
        this.unidadId = unidadId;
    }

    public Integer getTipoRecursoId() {
        return tipoRecursoId;
    }

    public void setTipoRecursoId(Integer tipoRecursoId) {
        this.tipoRecursoId = tipoRecursoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}