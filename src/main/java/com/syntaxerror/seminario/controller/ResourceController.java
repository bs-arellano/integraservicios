package com.syntaxerror.seminario.controller;

import com.syntaxerror.seminario.dto.ResourceCreationRequest;
import com.syntaxerror.seminario.dto.ResourceTypeCreationRequest;
import com.syntaxerror.seminario.model.Empleado;
import com.syntaxerror.seminario.model.Recurso;
import com.syntaxerror.seminario.model.TipoRecurso;
import com.syntaxerror.seminario.service.JwtUtil;
import com.syntaxerror.seminario.service.ResourceTypeManager;
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


    //POST method for resource creation
    @PostMapping("/resource")
    public ResponseEntity<String> createResource(@RequestBody ResourceCreationRequest request, @RequestHeader("Authorization") String authHeader){
        try {
            String jwt = authHeader.replace("Bearer ", "");
            //Validates request
            if(!serviceUnitManager.validateRequest(jwt, request.getServiceUnitID())){
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

    //GET method for all resources
    @GetMapping("/resource")
    public ResponseEntity<?> getResources(@RequestHeader("Authorization") String authHeader){
        try {
            String jwt = authHeader.replace("Bearer ", "");
            Map<String, String> decodedToken = JwtUtil.decodeToken(jwt);
            return ResponseEntity.ok(resourceManager.getAllResources());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //GET method for resource by ID
    @GetMapping("/resource/{id}")
    public ResponseEntity<?> getResourceByID(@RequestHeader("Authorization") String authHeader, @PathVariable Long id){
        try {
            String jwt = authHeader.replace("Bearer ", "");
            return ResponseEntity.ok(resourceManager.getResource(id));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //GET method for resources by service unit
    @GetMapping("/serviceunit/{service-unit-id}/resource")
    public ResponseEntity<?> getResourceByServiceUnit(@RequestHeader("Authorization") String authHeader, @PathVariable("service-unit-id") Long serviceUnitID){
        try {
            String jwt = authHeader.replace("Bearer ", "");
            return ResponseEntity.ok(resourceManager.getResourceByServiceUnit(serviceUnitID));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //GET method for resources by type
    @GetMapping("/resourcetype/{resource-type-id}/resource")
    public ResponseEntity<?> getResourceByType(@RequestHeader("Authorization") String authHeader, @PathVariable("resource-type-id") Long resourceTypeID){
        try {
            String jwt = authHeader.replace("Bearer ", "");
            return ResponseEntity.ok(resourceManager.getResourceByType(resourceTypeID));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
