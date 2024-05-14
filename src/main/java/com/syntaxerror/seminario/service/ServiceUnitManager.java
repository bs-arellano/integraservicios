package com.syntaxerror.seminario.service;

import com.syntaxerror.seminario.model.Empleado;
import com.syntaxerror.seminario.model.Usuario;
import com.syntaxerror.seminario.model.UnidadServicio;
import com.syntaxerror.seminario.repository.UnidadServicioRepository;
import com.syntaxerror.seminario.repository.UsuarioRepository;
import com.syntaxerror.seminario.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.Objects;

@Service
public class ServiceUnitManager {

    private final UnidadServicioRepository unidadServicioRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmpleadoRepository empleadoRepository;

    public ServiceUnitManager(UnidadServicioRepository unidadServicioRepository, UsuarioRepository usuarioRepository, EmpleadoRepository empleadoRepository) {
        this.unidadServicioRepository = unidadServicioRepository;
        this.usuarioRepository = usuarioRepository;
        this.empleadoRepository = empleadoRepository;
    }

    public UnidadServicio createServiceUnit(String name, Time startWorkingHours, Time endWorkingHours) {
        UnidadServicio unidadServicio = new UnidadServicio();
        unidadServicio.setNombre(name);
        unidadServicio.setHorarioLaboralInicio(startWorkingHours);
        unidadServicio.setHorarioLaboralFin(endWorkingHours);
        return unidadServicioRepository.save(unidadServicio);
    }
    public Empleado hireEmployee(Long employeeId, Long serviceUnitId) {
        Usuario usuario = usuarioRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        UnidadServicio serviceUnit = unidadServicioRepository.findById(serviceUnitId).orElseThrow(() -> new RuntimeException("Unidad de servicio no encontrada"));
        //Checks if the user is already an employee
        if (empleadoRepository.findByEmpleadoId(employeeId) != null) {
            throw new RuntimeException("El usuario ya es un empleado");
        }
        Empleado empleado = new Empleado();
        empleado.setEmpleadoId(usuario.getUsuarioId());
        empleado.setUnidadId(serviceUnit.getUnidadId());
        return empleadoRepository.save(empleado);
    }
    public void fireEmployee(Long employeeId) {
        Empleado empleado = empleadoRepository.findByEmpleadoId(employeeId);
        if (empleado == null) {
            throw new RuntimeException("Empleado no encontrado");
        }
        empleadoRepository.delete(empleado);
    }
    public Boolean checkEmployee(Long employeeId, Long serviceUnitId) {
        Empleado empleado = empleadoRepository.findByEmpleadoId(employeeId);
        if (empleado == null) {
            return false;
        }
        return Objects.equals(empleado.getUnidadId(), serviceUnitId);
    }
}