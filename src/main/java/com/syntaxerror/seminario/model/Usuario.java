package com.syntaxerror.seminario.model;

import jakarta.persistence.*;

@Entity
@Table(name = "\"Usuarios\"")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "rol", nullable = false, length = 50)
    private String rol;

    @Column(name = "convenio_id")
    private Integer convenioId;

    @Column(name = "id_externa", length = 100)
    private String idExterna;

    // Getters and setters
    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Integer getConvenioId() {
        return convenioId;
    }

    public void setConvenioId(Integer convenioId) {
        this.convenioId = convenioId;
    }

    public String getIdExterna() {
        return idExterna;
    }

    public void setIdExterna(String idExterna) {
        this.idExterna = idExterna;
    }
}
