package com.syntaxerror.seminario.controller;

import com.syntaxerror.seminario.dto.ResourceCreationRequest;
import com.syntaxerror.seminario.dto.ResourceTypeCreationRequest;
import com.syntaxerror.seminario.model.Recurso;
import com.syntaxerror.seminario.model.TipoRecurso;
import com.syntaxerror.seminario.service.JwtUtil;
import com.syntaxerror.seminario.service.ServiceUnitManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.syntaxerror.seminario.service.ResourceManager;

import java.net.URI;
import java.sql.Time;
import java.util.Map;

@RestController
public class ResourceController {
    private final ResourceManager resourceManager;
    private final ServiceUnitManager serviceUnitManager;
    public ResourceController(ResourceManager resourceManager, ServiceUnitManager serviceUnitManager) {
        this.resourceManager = resourceManager;
        this.serviceUnitManager = serviceUnitManager;
    }

    //POST method for resource type creation
    @PostMapping("/resourcetype")
    public ResponseEntity<String> createResourceType(@RequestBody ResourceTypeCreationRequest request, @RequestHeader("Authorization") String authHeader){
        try {
            String jwt = authHeader.replace("Bearer ", "");
            //Validates request
            if(!validateRequest(jwt, request.getServiceUnitID())){
                return ResponseEntity.badRequest().body("Usuario no autorizado para realizar esta acción");
            }
            Long serviceUnitID = request.getServiceUnitID();
            String name = request.getName();
            String description = request.getDescription();
            Time minLoanTime = request.getMinLoanTime();
            TipoRecurso tipoRecurso = resourceManager.createResourceType(serviceUnitID, name, description, minLoanTime);
            return ResponseEntity.created(new URI("/resourcetype/" + tipoRecurso.getTipoRecursoId())).body("Tipo de recurso creado exitosamente!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //GET method for resource types by service unit
    @GetMapping("/resourcetypes")
    public ResponseEntity<?> getResourceTypes(@RequestHeader("Authorization") String authHeader, @RequestParam("service-unit") Long serviceUnitID){
        try {
            String jwt = authHeader.replace("Bearer ", "");
            //Validates request
            if(!validateRequest(jwt, serviceUnitID)){
                return ResponseEntity.badRequest().body("Usuario no autorizado para realizar esta acción");
            }
            return ResponseEntity.ok(resourceManager.getResourceTypes(serviceUnitID));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //POST method for resource creation
    @PostMapping("/resource")
    public ResponseEntity<String> createResource(@RequestBody ResourceCreationRequest request, @RequestHeader("Authorization") String authHeader){
        try {
            String jwt = authHeader.replace("Bearer ", "");
            //Validates request
            if(!validateRequest(jwt, request.getServiceUnitID())){
                return ResponseEntity.badRequest().body("Usuario no autorizado para realizar esta acción");
            }
            Long serviceUnitID = request.getServiceUnitID();
            Long resourceTypeID = request.getResourceTypeID();
            String name = request.getName();
            String description = request.getDescription();
            Recurso recurso = resourceManager.createResource(serviceUnitID, resourceTypeID, name, description);
            return ResponseEntity.created(new URI("/resource/" + recurso.getRecursoId())).body("Recurso creado exitosamente!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public boolean validateRequest(String token, Long serviceUnitID){
        Map<String, String> decodedToken = JwtUtil.decodeToken(token);
        //Checks if the user is an admin or is an employee of the service unit
        if (!decodedToken.get("rol").equals("admin")) {
            return serviceUnitManager.checkEmployee(Long.parseLong(decodedToken.get("id")), serviceUnitID);
        }
        return true;
    }
}
