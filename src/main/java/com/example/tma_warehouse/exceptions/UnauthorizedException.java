package com.example.tma_warehouse.exceptions;

public class UnauthorizedException extends RuntimeException {

    private final String entityName;

    public UnauthorizedException(String entityName, String message) {
        super(message);
        this.entityName = entityName;
    }
    public String getEntityName() {
        return entityName;
    }

}

