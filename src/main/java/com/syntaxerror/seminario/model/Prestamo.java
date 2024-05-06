package com.syntaxerror.seminario.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "\"Prestamos\"")
public class Prestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prestamo_id")
    private Long prestamoId;

    @Column(name = "reserva_id", nullable = false, unique = true)
    private Integer reservaId;

    @Column(name = "empleado_id", nullable = false)
    private Integer empleadoId;

    @Column(name = "hora_entrega", nullable = false)
    private Timestamp horaEntrega;

    @Column(name = "hora_devolucion")
    private Timestamp horaDevolucion;

    @Column(name = "estado", nullable = false)
    private EstadoTransaccion estado;

    // Getters and setters
    public Long getPrestamoId() {
        return prestamoId;
    }

    public void setPrestamoId(Long prestamoId) {
        this.prestamoId = prestamoId;
    }

    public Integer getReservaId() {
        return reservaId;
    }

    public void setReservaId(Integer reservaId) {
        this.reservaId = reservaId;
    }

    public Integer getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(Integer empleadoId) {
        this.empleadoId = empleadoId;
    }

    public Timestamp getHoraEntrega() {
        return horaEntrega;
    }

    public void setHoraEntrega(Timestamp horaEntrega) {
        this.horaEntrega = horaEntrega;
    }

    public Timestamp getHoraDevolucion() {
        return horaDevolucion;
    }

    public void setHoraDevolucion(Timestamp horaDevolucion) {
        this.horaDevolucion = horaDevolucion;
    }

    public EstadoTransaccion getEstado() {
        return estado;
    }

    public void setEstado(EstadoTransaccion estado) {
        this.estado = estado;
    }
}