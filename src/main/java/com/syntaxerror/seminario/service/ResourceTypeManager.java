package com.syntaxerror.seminario.service;

import com.syntaxerror.seminario.model.TipoRecurso;
import com.syntaxerror.seminario.model.UnidadServicio;
import com.syntaxerror.seminario.repository.TipoRecursoRepository;
import com.syntaxerror.seminario.repository.UnidadServicioRepository;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;

@Service
public class ResourceTypeManager {
    private final TipoRecursoRepository tipoRecursoRepository;
    private final UnidadServicioRepository unidadServicioRepository;
    public ResourceTypeManager(TipoRecursoRepository tipoRecursoRepository, UnidadServicioRepository unidadServicioRepository){
        this.tipoRecursoRepository = tipoRecursoRepository;
        this.unidadServicioRepository = unidadServicioRepository;
    }
    public TipoRecurso createResourceType(Long serviceUnitID, String name, String description, Time minLoanTime) {
        UnidadServicio serviceUnit = unidadServicioRepository.findById(serviceUnitID).orElseThrow(() -> new RuntimeException("Unidad de servicio no encontrada"));
        TipoRecurso tipoRecurso = new TipoRecurso();
        tipoRecurso.setUnidadId(serviceUnit.getUnidadId());
        tipoRecurso.setNombre(name);
        tipoRecurso.setDescripcion(description);
        tipoRecurso.setTiempoMinimoPrestamo(minLoanTime);
        return tipoRecursoRepository.save(tipoRecurso);
    }
    public TipoRecurso getResourceType(Long resourceTypeID){
        return tipoRecursoRepository.findById(resourceTypeID).orElseThrow(() -> new RuntimeException("Tipo de recurso no encontrado"));
    }
    public List<TipoRecurso> getResourceTypes() {
        return tipoRecursoRepository.findAll();
    }
    public List<TipoRecurso> getServiceUnitResourceTypes(Long serviceUnitID){
        return tipoRecursoRepository.findByUnidadId(serviceUnitID);
    }
}
