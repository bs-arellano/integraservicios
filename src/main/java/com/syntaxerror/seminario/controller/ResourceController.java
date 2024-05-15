package com.syntaxerror.seminario.controller;

import com.syntaxerror.seminario.model.TipoRecurso;
import com.syntaxerror.seminario.service.JwtUtil;
import com.syntaxerror.seminario.service.ServiceUnitManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.syntaxerror.seminario.service.ResourceManager;

import java.sql.Time;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@RestController
public class ResourceController {
    private final ResourceManager resourceManager;
    private final ServiceUnitManager serviceUnitManager;
    public ResourceController(ResourceManager resourceManager, ServiceUnitManager serviceUnitManager) {
        this.resourceManager = resourceManager;
        this.serviceUnitManager = serviceUnitManager;
    }
    //Health check
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck(){
        return ResponseEntity.ok("Resource controller is up and running");
    }

    //POST method for resource type creation
    @PostMapping("/resourcetype")
    public ResponseEntity<String> createResourceType(@RequestBody ResourceTypeCreationRequest request, @RequestHeader("Authorization") String jwt){
        try {
            //Validates request
            if(!validateRequest(jwt, request.getServiceUnitID())){
                return ResponseEntity.badRequest().body("Usuario no autorizado para realizar esta acción");
            }
            Long serviceUnitID = request.getServiceUnitID();
            String name = request.getName();
            String description = request.getDescription();
            Time minLoanTime = request.getMinLoanTime();
            resourceManager.createResourceType(serviceUnitID, name, description, minLoanTime);
            return ResponseEntity.ok("Tipo de recurso creado exitosamente!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Health check 2
    @GetMapping("/resourcetypes")
    public ResponseEntity<?> getResourceTypes(@RequestHeader("x-authorization-token") String jwt, @RequestParam("service-unit") Long serviceUnitID){
        /*return ResponseEntity.ok("token: "+jwt+"\n"+"serviceUnitID: "+serviceUnitID);
    }*/
        System.out.println("token: "+jwt+"\n"+"serviceUnitID: "+serviceUnitID);
        try {
            System.out.println(jwt);
            //Validates request
            if(!validateRequest(jwt, serviceUnitID)){
                System.out.println("Usuario no autorizado para realizar esta acción");
                return ResponseEntity.badRequest().body("Usuario no autorizado para realizar esta acción");
            }
            return ResponseEntity.ok(resourceManager.getResourceTypes(serviceUnitID));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //POST method for resource creation
    @PostMapping("/resource")
    public ResponseEntity<String> createResource(@RequestBody ResourceCreationRequest request, @RequestHeader("Authorization") String jwt){
        try {
            //Validates request
            System.out.println(jwt);
            if(!validateRequest(jwt, request.getServiceUnitID())){
                System.out.println("Usuario no autorizado para realizar esta acción");
                return ResponseEntity.badRequest().body("Usuario no autorizado para realizar esta acción");
            }
            Long serviceUnitID = request.getServiceUnitID();
            Long resourceTypeID = request.getResourceTypeID();
            String name = request.getName();
            String description = request.getDescription();
            resourceManager.createResource(serviceUnitID, resourceTypeID, name, description);
            return ResponseEntity.ok("Recurso creado exitosamente!");
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

    public static class ResourceTypeCreationRequest {
        private Long serviceUnitID;
        private String name;
        private String description;
        private Time minLoanTime;
        // Getters
        public Long getServiceUnitID() {
            return serviceUnitID;
        }
        public String getName() {
            return name;
        }
        public String getDescription() {
            return description;
        }
        public Time getMinLoanTime() {
            return minLoanTime;
        }
    }
    public static class ResourceCreationRequest {
        private Long serviceUnitID;
        private Long resourceTypeID;
        private String name;
        private String description;
        // Getters
        public Long getServiceUnitID() {
            return serviceUnitID;
        }
        public Long getResourceTypeID() {
            return resourceTypeID;
        }
        public String getName() {
            return name;
        }
        public String getDescription() {
            return description;
        }
    }
}
