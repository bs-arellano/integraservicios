package com.syntaxerror.seminario.dto;

import java.sql.Time;

public class ResourceTypeCreationRequest {
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