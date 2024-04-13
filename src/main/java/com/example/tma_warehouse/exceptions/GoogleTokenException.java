package com.example.tma_warehouse.exceptions;

public class GoogleTokenException extends RuntimeException {

    private final String entityName;

    public GoogleTokenException(String entityName, String message) {
        super(message);
        this.entityName = entityName;
    }
    public String getEntityName() {
        return entityName;
    }

}