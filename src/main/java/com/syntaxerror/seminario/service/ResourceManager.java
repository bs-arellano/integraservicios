package com.syntaxerror.seminario.service;

import com.syntaxerror.seminario.model.TipoRecurso;
import com.syntaxerror.seminario.model.Recurso;
import com.syntaxerror.seminario.model.UnidadServicio;
import com.syntaxerror.seminario.repository.RecursoRepository;
import com.syntaxerror.seminario.repository.TipoRecursoRepository;
import com.syntaxerror.seminario.repository.UnidadServicioRepository;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.Duration;
import java.util.List;

@Service
public class ResourceManager {

    private final TipoRecursoRepository tipoRecursoRepository;
    private final RecursoRepository recursoRepository;
    private final UnidadServicioRepository unidadServicioRepository;

    public ResourceManager(TipoRecursoRepository tipoRecursoRepository, RecursoRepository recursoRepository, UnidadServicioRepository unidadServicioRepository) {
        this.unidadServicioRepository = unidadServicioRepository;
        this.tipoRecursoRepository = tipoRecursoRepository;
        this.recursoRepository = recursoRepository;
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
    public List<TipoRecurso> getResourceTypes(Long serviceUnitID){
        return tipoRecursoRepository.findByUnidadId(serviceUnitID);
    }
    public TipoRecurso getResourceType(Long resourceTypeID){
        return tipoRecursoRepository.findById(resourceTypeID).orElseThrow(() -> new RuntimeException("Tipo de recurso no encontrado"));
    }
    public Recurso createResource(Long serviceUnitID, Long resourceTypeID, String name, String description) {
        UnidadServicio serviceUnit = unidadServicioRepository.findById(serviceUnitID).orElseThrow(() -> new RuntimeException("Unidad de servicio no encontrada"));
        TipoRecurso resourceType = tipoRecursoRepository.findById(resourceTypeID).orElseThrow(() -> new RuntimeException("Tipo de recurso no encontrado"));
        Recurso recurso = new Recurso();
        recurso.setUnidadId(serviceUnit.getUnidadId());
        recurso.setTipoRecursoId(resourceType.getTipoRecursoId());
        recurso.setNombre(name);
        recurso.setDescripcion(description);
        return recursoRepository.save(recurso);
    }
    public Recurso getResource(Long resourceID){
        return recursoRepository.findById(resourceID).orElseThrow(() -> new RuntimeException("Recurso no encontrado"));
    }
    public Recurso getResourceByServiceUnit(Long serviceUnitID){
        return recursoRepository.findByUnidadId(serviceUnitID);
    }
    public Recurso getResourceByType(Long resourceTypeID){
        return recursoRepository.findByTipoRecursoId(resourceTypeID);
    }
}