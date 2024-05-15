package com.syntaxerror.seminario.controller;

import com.syntaxerror.seminario.dto.ServiceUnitCreationRequest;
import com.syntaxerror.seminario.model.UnidadServicio;
import com.syntaxerror.seminario.service.JwtUtil;
import com.syntaxerror.seminario.service.ServiceUnitManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.util.Map;

@RestController
public class ServiceUnitController {
    private final ServiceUnitManager serviceUnitManager;
    public ServiceUnitController(ServiceUnitManager serviceUnitManager) {
        this.serviceUnitManager = serviceUnitManager;
    }

    // Create service unit endpoint
    @PostMapping("/serviceunit")
    public ResponseEntity<String> createServiceUnit(@RequestBody ServiceUnitCreationRequest request, @RequestHeader("Authorization") String jwt){
        try {
            //Decodes jwt
            Map<String, String> decodedToken = JwtUtil.decodeToken(jwt);
            //Checks if the user is an admin
            if (!decodedToken.get("rol").equals("admin")) {
                return ResponseEntity.badRequest().body("Usuario no autorizado para realizar esta acción");
            }
            UnidadServicio unidadServicio = serviceUnitManager.createServiceUnit(request.getName(), request.getStartWorkingHours(), request.getEndWorkingHours());
            return ResponseEntity.created(new URI("/serviceunit/" + unidadServicio.getUnidadId())).body("Unidad de servicio creada exitosamente!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
