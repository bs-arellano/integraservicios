package com.syntaxerror.seminario.dto;

import java.sql.Time;

public class ServiceUnitCreationRequest {
    private String name;
    private Time startWorkingHours;
    private Time endWorkingHours;
    // Getters
    public String getName() {
        return name;
    }
    public Time getStartWorkingHours() {
        return startWorkingHours;
    }
    public Time getEndWorkingHours() {
        return endWorkingHours;
    }
}