package com.syntaxerror.seminario.services;

import com.syntaxerror.seminario.model.HorarioDisponibilidad;
import com.syntaxerror.seminario.model.Recurso;
import com.syntaxerror.seminario.model.Reserva;
import com.syntaxerror.seminario.model.TipoRecurso;
import com.syntaxerror.seminario.repository.RecursoRepository;
import com.syntaxerror.seminario.repository.ReservaRepository;

import com.syntaxerror.seminario.service.BookingManager;
import com.syntaxerror.seminario.service.ServiceUnitManager;
import com.syntaxerror.seminario.service.ResourceTypeManager;


import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingManagerTest {

    @Mock
    ReservaRepository reservaRepository;

    @Mock
    RecursoRepository recursoRepository;

    @Mock
    ServiceUnitManager serviceUnitManager;

    @Mock
    ResourceTypeManager resourceTypeManager;

    @InjectMocks
    BookingManager bookingManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void bookResourceSuccessfully() {
        TipoRecurso tipoRecurso = new TipoRecurso();
        tipoRecurso.setTiempoMinimoPrestamo(Time.valueOf("00:30:00"));
        Recurso recurso = new Recurso();
        recurso.setTipoRecursoId(1L);
        HorarioDisponibilidad horarioDisponibilidad = new HorarioDisponibilidad();
        horarioDisponibilidad.setHoraInicio(Time.valueOf(LocalTime.of(8, 0)));
        horarioDisponibilidad.setHoraFin(Time.valueOf(LocalTime.of(20, 0)));

        when(recursoRepository.findById(anyLong())).thenReturn(Optional.of(recurso));
        when(reservaRepository.findByRecursoId(anyLong())).thenReturn(Collections.emptyList());
        when(serviceUnitManager.checkActiveEmployee(anyLong(), anyLong())).thenReturn(Optional.empty());
        when(resourceTypeManager.getResourceType(anyLong())).thenReturn(tipoRecurso);
        when(resourceTypeManager.getScheduleByDay(anyLong(), any())).thenReturn(horarioDisponibilidad);
        when(reservaRepository.save(any(Reserva.class))).thenReturn(new Reserva());

        Reserva result = bookingManager.bookResource(1L, 1L, Date.valueOf(LocalDate.now()), Time.valueOf(LocalTime.now()), Time.valueOf(LocalTime.now().plusHours(1)));

        assertNotNull(result);
        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }

    @Test
    void bookResourceFailsWhenStartIsAfterEnd() {
        assertThrows(RuntimeException.class, () -> bookingManager.bookResource(1L, 1L, Date.valueOf(LocalDate.now()), Time.valueOf(LocalTime.now().plusHours(1)), Time.valueOf(LocalTime.now())));
    }

    @Test
    void bookResourceFailsWhenDateIsBeforeToday() {
        assertThrows(RuntimeException.class, () -> bookingManager.bookResource(1L, 1L, Date.valueOf(LocalDate.now().minusDays(1)), Time.valueOf(LocalTime.now()), Time.valueOf(LocalTime.now().plusHours(1))));
    }

    @Test
    void bookResourceFailsWhenResourceDoesNotExist() {
        when(recursoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookingManager.bookResource(1L, 1L, Date.valueOf(LocalDate.now()), Time.valueOf(LocalTime.now()), Time.valueOf(LocalTime.now().plusHours(1))));
    }
}