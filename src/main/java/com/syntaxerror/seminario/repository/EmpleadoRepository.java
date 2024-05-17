package com.syntaxerror.seminario.repository;

import com.syntaxerror.seminario.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long>{
    //Find an employee by ID
    Empleado findByEmpleadoId(Long empleadoId);
    //Find all employees by user
    List<Empleado> findByUsuarioId(Long userId);
    //Find all employees of a service unit
    List<Empleado> findByUnidadId(Long serviceUnitID);
}
