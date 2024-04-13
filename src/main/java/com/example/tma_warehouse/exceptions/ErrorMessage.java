package com.example.tma_warehouse.exceptions;

public record ErrorMessage(String fieldName, String error) {

    @Override
    public String toString() {
        return error;
    }

}
