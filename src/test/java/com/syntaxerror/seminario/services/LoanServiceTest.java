package com.syntaxerror.seminario.services;

import com.syntaxerror.seminario.model.EstadoTransaccion;
import com.syntaxerror.seminario.model.Prestamo;
import com.syntaxerror.seminario.repository.PrestamoRepository;
import com.syntaxerror.seminario.repository.ReservaRepository;
import com.syntaxerror.seminario.repository.EmpleadoRepository;
import com.syntaxerror.seminario.service.ServiceUnitManager;
import com.syntaxerror.seminario.model.Reserva;
import com.syntaxerror.seminario.model.Empleado;
import com.syntaxerror.seminario.service.LoanService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanServiceTest {

    @Mock
    PrestamoRepository prestamoRepository;

    @Mock
    ReservaRepository reservaRepository;

    @Mock
    ServiceUnitManager serviceUnitManager;

    @Mock
    EmpleadoRepository empleadoRepository;

    @InjectMocks
    LoanService loanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void checkInSuccessfully() {
        Empleado empleado = new Empleado();
        empleado.setUsuarioId(1L);
        empleado.setStatus(true);
        empleado.setUnidadId(1L);
        Reserva reserva = new Reserva();
        reserva.setEstado(EstadoTransaccion.COMPLETADA);

        when(reservaRepository.findById(anyLong())).thenReturn(Optional.of(reserva));
        when(empleadoRepository.findById(anyLong())).thenReturn(Optional.of(empleado));
        when(serviceUnitManager.checkActiveEmployee(empleado.getEmpleadoId(),  empleado.getUnidadId())).thenReturn(Optional.of(empleado));
        when(prestamoRepository.save(any(Prestamo.class))).thenReturn(new Prestamo());

        Prestamo result = loanService.checkIn(1L, 1L, new Timestamp(System.currentTimeMillis()));

        assertNotNull(result);
    }

    @Test
    void checkInFailsWhenReservaNotFound() {
        when(reservaRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> loanService.checkIn(1L, 1L, new Timestamp(System.currentTimeMillis())));
    }

    @Test
    void checkInFailsWhenEmpleadoNotFound() {
        when(reservaRepository.findById(anyLong())).thenReturn(Optional.of(new Reserva()));
        when(empleadoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> loanService.checkIn(1L, 1L, new Timestamp(System.currentTimeMillis())));
    }

    @Test
    void checkOutSuccessfully() {
        Empleado empleado = new Empleado();
        empleado.setUsuarioId(1L);
        empleado.setStatus(true);
        empleado.setUnidadId(1L);
        Reserva reserva = new Reserva();
        reserva.setEstado(EstadoTransaccion.COMPLETADA);

        when(reservaRepository.findById(anyLong())).thenReturn(Optional.of(reserva));
        when(empleadoRepository.findById(anyLong())).thenReturn(Optional.of(empleado));
        when(serviceUnitManager.checkActiveEmployee(empleado.getEmpleadoId(),  empleado.getUnidadId())).thenReturn(Optional.of(empleado));
        when(prestamoRepository.save(any(Prestamo.class))).thenReturn(new Prestamo());

        Prestamo result = loanService.checkIn(1L, 1L, new Timestamp(System.currentTimeMillis()));

        assertNotNull(result);
    }

    @Test
    void checkOutFailsWhenPrestamoNotFound() {
        when(prestamoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> loanService.checkOut(1L, 1L, new Timestamp(System.currentTimeMillis())));
    }

    @Test
    void checkOutFailsWhenEmpleadoNotFound() {
        when(prestamoRepository.findById(anyLong())).thenReturn(Optional.of(new Prestamo()));
        when(empleadoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> loanService.checkOut(1L, 1L, new Timestamp(System.currentTimeMillis())));
    }
}