package com.syntaxerror.seminario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.syntaxerror.seminario.model.Recurso;

import java.util.List;

@Repository
public interface RecursoRepository extends JpaRepository<Recurso, Long>{
    List<Recurso> findByUnidadId(Long serviceUnitID);

    List<Recurso> findByTipoRecursoId(Long resourceTypeID);
}
