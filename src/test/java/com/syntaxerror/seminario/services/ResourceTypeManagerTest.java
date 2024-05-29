package com.syntaxerror.seminario.services;

import com.syntaxerror.seminario.model.TipoRecurso;
import com.syntaxerror.seminario.model.UnidadServicio;
import com.syntaxerror.seminario.repository.HorarioDisponibilidadRepository;
import com.syntaxerror.seminario.repository.TipoRecursoRepository;
import com.syntaxerror.seminario.repository.UnidadServicioRepository;
import com.syntaxerror.seminario.service.ResourceTypeManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Time;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ResourceTypeManagerTest {

    @InjectMocks
    private ResourceTypeManager resourceTypeManager;

    @Mock
    private TipoRecursoRepository tipoRecursoRepository;

    @Mock
    private UnidadServicioRepository unidadServicioRepository;

    @Mock
    private HorarioDisponibilidadRepository horarioDisponibilidadRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnResourceTypesWhenServiceUnitIdIsProvided() {
        TipoRecurso tipoRecurso = new TipoRecurso();
        tipoRecurso.setTipoRecursoId(1L);
        tipoRecurso.setUnidadId(1L);
        tipoRecurso.setNombre("Proyector");
        tipoRecurso.setDescripcion("Proyector de alta definición");
        tipoRecurso.setTiempoMinimoPrestamo(Time.valueOf("02:00:00"));

        when(tipoRecursoRepository.findByUnidadId(1L)).thenReturn(Arrays.asList(tipoRecurso));

        List<TipoRecurso> actual = resourceTypeManager.getServiceUnitResourceTypes(1L);

        assertEquals(1, actual.size());
        assertEquals(tipoRecurso, actual.get(0));
    }

    @Test
    public void shouldCreateResourceTypeWhenValidDataIsProvided() {
        UnidadServicio serviceUnit = new UnidadServicio();
        serviceUnit.setNombre("Biblioteca");

        when(unidadServicioRepository.findById(1L)).thenReturn(java.util.Optional.of(serviceUnit));

        TipoRecurso tipoRecurso = new TipoRecurso();
        tipoRecurso.setUnidadId(serviceUnit.getUnidadId());
        tipoRecurso.setNombre("Proyector");
        tipoRecurso.setDescripcion("Proyector de alta definición");
        tipoRecurso.setTiempoMinimoPrestamo(Time.valueOf("02:00:00"));

        when(tipoRecursoRepository.save(any(TipoRecurso.class))).thenReturn(tipoRecurso);

        TipoRecurso actual = resourceTypeManager.createResourceType(1L, "Proyector", "Proyector de alta definición", Time.valueOf("02:00:00"));

        assertEquals(tipoRecurso, actual);
    }
}