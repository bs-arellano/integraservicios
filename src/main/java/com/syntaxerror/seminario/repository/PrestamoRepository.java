package com.syntaxerror.seminario.repository;

import com.syntaxerror.seminario.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
}
