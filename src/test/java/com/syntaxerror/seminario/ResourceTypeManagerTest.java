package com.syntaxerror.seminario;

import com.syntaxerror.seminario.model.TipoRecurso;
import com.syntaxerror.seminario.repository.TipoRecursoRepository;
import com.syntaxerror.seminario.service.ResourceTypeManager;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.Time;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class ResourceTypeManagerTest {
    @Mock
    private TipoRecursoRepository tipoRecursoRepository;
    @InjectMocks
    private ResourceTypeManager resourceTypeManager;
    @Test
    public void GetServiceUnitResourceTypesTest(){
        // Arrange
        TipoRecurso proyector = new TipoRecurso();
        proyector.setTipoRecursoId(1L);
        proyector.setUnidadId(1L);
        proyector.setNombre("Proyector");
        proyector.setDescripcion("Proyector de alta definición");
        proyector.setTiempoMinimoPrestamo(Time.valueOf("02:00:00"));

        TipoRecurso aula = new TipoRecurso();
        aula.setTipoRecursoId(2L);
        aula.setUnidadId(1L);
        aula.setNombre("Aula");
        aula.setDescripcion("Aula de conferencias");
        aula.setTiempoMinimoPrestamo(Time.valueOf("04:00:00"));

        when(tipoRecursoRepository.findByUnidadId(1L)).thenReturn(Arrays.asList(proyector, aula));

        List<TipoRecurso> expected = Arrays.asList(proyector, aula);

        // Act
        List<TipoRecurso> actual = resourceTypeManager.getServiceUnitResourceTypes(1L);
        // Assert
        assertEquals(expected, actual);
    }
}