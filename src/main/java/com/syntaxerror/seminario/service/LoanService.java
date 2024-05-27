package com.syntaxerror.seminario.service;

import com.syntaxerror.seminario.model.Empleado;
import com.syntaxerror.seminario.model.Prestamo;
import com.syntaxerror.seminario.model.Reserva;
import com.syntaxerror.seminario.model.EstadoTransaccion;
import com.syntaxerror.seminario.repository.EmpleadoRepository;
import com.syntaxerror.seminario.repository.PrestamoRepository;
import com.syntaxerror.seminario.repository.ReservaRepository;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;

@Service
public class LoanService {
    private final PrestamoRepository prestamoRepository;
    private final ReservaRepository reservaRepository;
    private final ServiceUnitManager serviceUnitManager;
    private final EmpleadoRepository empleadoRepository;
    public LoanService(PrestamoRepository prestamoRepository, ReservaRepository reservaRepository, ServiceUnitManager serviceUnitManager, EmpleadoRepository empleadoRepository) {
        this.prestamoRepository = prestamoRepository;
        this.reservaRepository = reservaRepository;
        this.serviceUnitManager = serviceUnitManager;
        this.empleadoRepository = empleadoRepository;
    }

    public Prestamo checkIn(Long reservaId, Long empleadoId, Timestamp horaEntrega) {
        // Retrieve the Reserva from the repository
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        if (reserva.getEstado().equals(EstadoTransaccion.CANCELADA)) {
            throw new RuntimeException("Reserva cancelada");
        }
        // Retrieve the Empleado from the repository
        Empleado empleado = empleadoRepository.findById(empleadoId)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        serviceUnitManager.checkActiveEmployee(empleado.getEmpleadoId(), empleado.getUnidadId());

        // Set booking as completed
        reserva.setEstado(EstadoTransaccion.COMPLETADA);

        // Create a new Prestamo
        Prestamo prestamo = new Prestamo();
        prestamo.setReservaId(reserva.getReservaId());
        prestamo.setEmpleadoId(empleado.getEmpleadoId());
        prestamo.setHoraEntrega(horaEntrega);
        prestamo.setEstado(EstadoTransaccion.ACTIVA);

        // Save the Prestamo to the repository
        return prestamoRepository.save(prestamo);
    }

    public Prestamo checkOut(Long prestamoId, Timestamp horaDevolucion) {
        // Retrieve the Prestamo from the repository
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new RuntimeException("Prestamo no encontrado"));

        // Update the Prestamo
        prestamo.setHoraDevolucion(horaDevolucion);
        prestamo.setEstado(EstadoTransaccion.COMPLETADA);

        // Save the updated Prestamo to the repository
        return prestamoRepository.save(prestamo);
    }
}
