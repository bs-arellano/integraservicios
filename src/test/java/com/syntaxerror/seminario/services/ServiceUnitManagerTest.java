package com.syntaxerror.seminario.services;

import com.syntaxerror.seminario.model.Empleado;
import com.syntaxerror.seminario.model.UnidadServicio;
import com.syntaxerror.seminario.model.Usuario;
import com.syntaxerror.seminario.repository.EmpleadoRepository;
import com.syntaxerror.seminario.repository.UnidadServicioRepository;
import com.syntaxerror.seminario.repository.UsuarioRepository;
import com.syntaxerror.seminario.service.ServiceUnitManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Time;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServiceUnitManagerTest {

    @Mock
    UnidadServicioRepository unidadServicioRepository;

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    EmpleadoRepository empleadoRepository;

    @InjectMocks
    ServiceUnitManager serviceUnitManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createServiceUnitSuccessfully() {
        when(unidadServicioRepository.save(any(UnidadServicio.class))).thenReturn(new UnidadServicio());

        UnidadServicio result = serviceUnitManager.createServiceUnit("name", Time.valueOf("08:00:00"), Time.valueOf("17:00:00"));

        assertNotNull(result);
        verify(unidadServicioRepository, times(1)).save(any(UnidadServicio.class));
    }

    @Test
    void hireEmployeeSuccessfully() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(new Usuario()));
        when(unidadServicioRepository.findById(anyLong())).thenReturn(Optional.of(new UnidadServicio()));
        when(empleadoRepository.findByUnidadId(anyLong())).thenReturn(Collections.emptyList());
        when(empleadoRepository.save(any(Empleado.class))).thenReturn(new Empleado());

        Empleado result = serviceUnitManager.hireEmployee(1L, 1L, "cargo");

        assertNotNull(result);
        verify(empleadoRepository, times(1)).save(any(Empleado.class));
    }

    @Test
    void checkActiveEmployeeSuccessfully() {
        when(empleadoRepository.findByUnidadId(anyLong())).thenReturn(Collections.singletonList(new Empleado()));

        Optional<Empleado> result = serviceUnitManager.checkActiveEmployee(1L, 1L);

        assertTrue(result.isPresent());
    }

    @Test
    void getServiceUnitActiveEmployeesSuccessfully() {
        when(empleadoRepository.findByUnidadId(anyLong())).thenReturn(Collections.singletonList(new Empleado()));

        var result = serviceUnitManager.getServiceUnitActiveEmployees(1L);

        assertFalse(result.isEmpty());
    }
}