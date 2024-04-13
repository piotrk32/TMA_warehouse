package com.example.tma_warehouse.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

    private final String fieldName;

    public UserAlreadyExistsException(String entityName, String message) {
        super(message);
        this.fieldName = entityName;
    }
    public String getFieldName() {
        return fieldName;
    }

}
