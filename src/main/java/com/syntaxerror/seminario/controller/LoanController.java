package com.syntaxerror.seminario.controller;

import com.syntaxerror.seminario.model.Empleado;
import com.syntaxerror.seminario.model.Prestamo;
import com.syntaxerror.seminario.service.JwtUtil;
import com.syntaxerror.seminario.service.LoanService;
import com.syntaxerror.seminario.service.ServiceUnitManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Map;

@RestController
public class LoanController {
    private final LoanService loanService;
    private final ServiceUnitManager serviceUnitManager;
    private final JwtUtil jwtUtil;

    public LoanController(LoanService loanService, ServiceUnitManager serviceUnitManager, JwtUtil jwtUtil) {
        this.loanService = loanService;
        this.serviceUnitManager = serviceUnitManager;
        this.jwtUtil = jwtUtil;
    }

    // Check in a book
    @PostMapping("/booking/{id}/checkin")
    public ResponseEntity<Prestamo> checkInBook(@PathVariable("id") Long bookingId, @RequestHeader("Authorization") String authHeader){
        try{
            String jwt = authHeader.replace("Bearer ", "");
            Map<String, String> decodedToken = jwtUtil.decodeToken(jwt);
            Long userId = Long.parseLong(decodedToken.get("id"));
            Empleado empleado = serviceUnitManager.getActiveEmployment(userId);
            if(empleado == null){
                return ResponseEntity.badRequest().build();
            }
            Prestamo prestamo = loanService.checkIn(bookingId, empleado.getEmpleadoId(), new Timestamp(System.currentTimeMillis()));
            return ResponseEntity.ok(prestamo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    // Check out a book
    @PostMapping("/booking/{id}/checkout")
    public ResponseEntity<Prestamo> checkOutBook(@PathVariable("id") Long bookingId, @RequestHeader("Authorization") String authHeader){
        try{
            String jwt = authHeader.replace("Bearer ", "");
            Map<String, String> decodedToken = jwtUtil.decodeToken(jwt);
            Long userId = Long.parseLong(decodedToken.get("id"));
            Empleado empleado = serviceUnitManager.getActiveEmployment(userId);
            if(empleado == null){
                return ResponseEntity.badRequest().build();
            }
            Prestamo prestamo = loanService.checkOut(bookingId, empleado.getEmpleadoId(), new Timestamp(System.currentTimeMillis()));
            return ResponseEntity.ok(prestamo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
