package com.syntaxerror.seminario.controller;

import com.syntaxerror.seminario.dto.BookResourceRequest;
import com.syntaxerror.seminario.model.Recurso;
import com.syntaxerror.seminario.model.Reserva;
import com.syntaxerror.seminario.service.BookingManager;
import com.syntaxerror.seminario.service.JwtUtil;
import com.syntaxerror.seminario.service.ResourceManager;
import com.syntaxerror.seminario.service.ServiceUnitManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class BookingController {

    private final BookingManager bookingManager;
    private final ResourceManager resourceManager;
    private final ServiceUnitManager serviceUnitManager;
    private final JwtUtil jwtUtil;

    public BookingController(BookingManager bookingManager, ServiceUnitManager serviceUnitManager, ResourceManager resourceManager, JwtUtil jwtUtil) {
        this.bookingManager = bookingManager;
        this.serviceUnitManager = serviceUnitManager;
        this.resourceManager = resourceManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/resource/{id}/book")
    public ResponseEntity<String> bookResource(@RequestBody BookResourceRequest request, @PathVariable("id") Long resourceId, @RequestHeader("Authorization") String authHeader){
        try {
            String jwt = authHeader.replace("Bearer ", "");
            //Validates request
            Map<String, String> decodedToken = jwtUtil.decodeToken(jwt);
            if (!decodedToken.get("id").equals(request.getUserId().toString())) {
                Recurso resource = resourceManager.getResource(resourceId);
                if (serviceUnitManager.checkActiveEmployee(request.getUserId(), resource.getUnidadId()).isEmpty()){
                    return ResponseEntity.badRequest().body("El usuario no tiene permisos para reservar este recurso");
                }
            }
            Reserva reserva = bookingManager.bookResource(request.getUserId(), resourceId, request.getDate(), request.getStart(), request.getEnd());
            System.out.println(reserva.toString());
            return ResponseEntity.ok().body("Reserva creada con éxito. ID: " + reserva.getReservaId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/resource/{id}/bookings")
    public ResponseEntity<List<Reserva>> getBookings(@PathVariable("id") Long resourceId, @RequestHeader("Authorization") String authHeader){
        try {
            String jwt = authHeader.replace("Bearer ", "");
            //Validates request
            Map<String, String> decodedToken = jwtUtil.decodeToken(jwt);
            Recurso resource = resourceManager.getResource(resourceId);
            if (serviceUnitManager.checkActiveEmployee(Long.parseLong(decodedToken.get("id")), resource.getUnidadId()).isEmpty()){
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok().body(bookingManager.getBookingsByResource(resourceId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{id}/bookings")
    public ResponseEntity<List<Reserva>> getBookingsByUser(@PathVariable("id") Long userId, @RequestHeader("Authorization") String authHeader){
        try {
            String jwt = authHeader.replace("Bearer ", "");
            //Validates request
            Map<String, String> decodedToken = jwtUtil.decodeToken(jwt);
            if (!decodedToken.get("id").equals(userId.toString())) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok().body(bookingManager.getBookingsByUser(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/booking/{id}")
    public ResponseEntity<Reserva> getBooking(@PathVariable("id") Long bookingId, @RequestHeader("Authorization") String authHeader){
        try {
            String jwt = authHeader.replace("Bearer ", "");
            //Validates request
            Map<String, String> decodedToken = jwtUtil.decodeToken(jwt);
            Reserva reserva = bookingManager.getBooking(bookingId);
            if (!decodedToken.get("id").equals(reserva.getUsuarioId().toString())) {
                Recurso resource = resourceManager.getResource(reserva.getRecursoId());
                if (serviceUnitManager.checkActiveEmployee(Long.parseLong(decodedToken.get("id")), resource.getUnidadId()).isEmpty()){
                    return ResponseEntity.badRequest().build();
                }
            }
            return ResponseEntity.ok().body(reserva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
