package com.syntaxerror.seminario.controller;

import com.syntaxerror.seminario.dto.ResourceTypeCreationRequest;
import com.syntaxerror.seminario.model.TipoRecurso;
import com.syntaxerror.seminario.service.ResourceTypeManager;
import com.syntaxerror.seminario.service.ServiceUnitManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.Time;

@RestController
public class ResourceTypeController {
    private final ResourceTypeManager resourceTypeManager;
    private final ServiceUnitManager serviceUnitManager;
    public ResourceTypeController(ResourceTypeManager resourceTypeManager, ServiceUnitManager serviceUnitManager){
        this.resourceTypeManager = resourceTypeManager;
        this.serviceUnitManager = serviceUnitManager;
    }
    //POST method for resource type creation
    @PostMapping("/resourcetype")
    public ResponseEntity<String> createResourceType(@RequestBody ResourceTypeCreationRequest request, @RequestHeader("Authorization") String authHeader){
        try {
            String jwt = authHeader.replace("Bearer ", "");
            //Validates request
            if(!serviceUnitManager.validateRequest(jwt, request.getServiceUnitID())){
                return ResponseEntity.badRequest().body("Usuario no autorizado para realizar esta acción");
            }
            Long serviceUnitID = request.getServiceUnitID();
            String name = request.getName();
            String description = request.getDescription();
            Time minLoanTime = request.getMinLoanTime();
            TipoRecurso tipoRecurso = resourceTypeManager.createResourceType(serviceUnitID, name, description, minLoanTime);
            return ResponseEntity.created(new URI("/resourcetype/" + tipoRecurso.getTipoRecursoId())).body("Tipo de recurso creado exitosamente!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //GET method for all resource types
    @GetMapping("/resourcetype")
    public ResponseEntity<?> getResourceTypes(@RequestHeader("Authorization") String authHeader){
        try {
            String jwt = authHeader.replace("Bearer ", "");
            return ResponseEntity.ok(resourceTypeManager.getResourceTypes());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //GET method for resource type by ID
    @GetMapping("/resourcetype/{id}")
    public ResponseEntity<?> getResourceTypeByID(@RequestHeader("Authorization") String authHeader, @PathVariable Long id){
        try {
            String jwt = authHeader.replace("Bearer ", "");
            return ResponseEntity.ok(resourceTypeManager.getResourceType(id));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //GET method for resource types by service unit
    @GetMapping("/serviceunit/{service-unit-id}/resourcetype")
    public ResponseEntity<?> getServiceUnitResourceTypes(@RequestHeader("Authorization") String authHeader, @PathVariable("service-unit-id") Long serviceUnitID){
        try {
            String jwt = authHeader.replace("Bearer ", "");
            return ResponseEntity.ok(resourceTypeManager.getServiceUnitResourceTypes(serviceUnitID));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
