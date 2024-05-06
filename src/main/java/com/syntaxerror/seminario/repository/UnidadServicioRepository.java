package com.syntaxerror.seminario.repository;

import com.syntaxerror.seminario.model.UnidadServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnidadServicioRepository extends JpaRepository<UnidadServicio, Long> {
}