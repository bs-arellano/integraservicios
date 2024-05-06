package com.syntaxerror.seminario.repository;

import com.syntaxerror.seminario.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long>{
    //findByUserId
    Empleado findByEmpleadoId(Long empleadoId);
}
