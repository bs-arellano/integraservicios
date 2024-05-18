package com.syntaxerror.seminario.repository;

import com.syntaxerror.seminario.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    public List<Reserva> findByRecursoId(Long recursoId);
}
