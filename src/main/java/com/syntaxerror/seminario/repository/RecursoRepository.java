package com.syntaxerror.seminario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.syntaxerror.seminario.model.Recurso;

@Repository
public interface RecursoRepository extends JpaRepository<Recurso, Long>{
    Recurso findByUnidadId(Long serviceUnitID);

    Recurso findByTipoRecursoId(Long resourceTypeID);
}
