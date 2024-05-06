package com.syntaxerror.seminario.model;

import jakarta.persistence.*;

import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "\"Reservas\"")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reserva_id")
    private Long reservaId;

    @Column(name = "usuario_id", nullable = false)
    private Integer usuarioId;

    @Column(name = "recurso_id", nullable = false)
    private Integer recursoId;

    @Column(name = "fecha_reserva", nullable = false)
    private Date fechaReserva;

    @Column(name = "hora_inicio_reserva", nullable = false)
    private Time horaInicioReserva;

    @Column(name = "hora_fin_reserva", nullable = false)
    private Time horaFinReserva;

    @Column(name = "estado", nullable = false)
    private EstadoTransaccion estado;

    // Getters and setters
    public Long getReservaId() {
        return reservaId;
    }

    public void setReservaId(Long reservaId) {
        this.reservaId = reservaId;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Integer getRecursoId() {
        return recursoId;
    }

    public void setRecursoId(Integer recursoId) {
        this.recursoId = recursoId;
    }

    public Date getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(Date fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public Time getHoraInicioReserva() {
        return horaInicioReserva;
    }

    public void setHoraInicioReserva(Time horaInicioReserva) {
        this.horaInicioReserva = horaInicioReserva;
    }

    public Time getHoraFinReserva() {
        return horaFinReserva;
    }

    public void setHoraFinReserva(Time horaFinReserva) {
        this.horaFinReserva = horaFinReserva;
    }

    public EstadoTransaccion getEstado() {
        return estado;
    }

    public void setEstado(EstadoTransaccion estado) {
        this.estado = estado;
    }
}