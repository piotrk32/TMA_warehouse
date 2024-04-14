package com.example.tma_warehouse.models.request.dtos;

import com.example.tma_warehouse.models.request.Request;

public class RequestMapper {

    public static RequestResponseDTO mapToRequestResponseDTO(Request request) {
        String fullName = request.getEmployee().getFirstName() + " " + request.getEmployee().getLastName();

        return RequestResponseDTO
                .builder()
                .requestId(request.getId())
                .employeeName(fullName)
                .itemId(request.getItem().getId())
                .unitOfMeasurement(request.getUnitOfMeasurement().name())
                .quantity(request.getQuantity())
                .priceWithoutVat(request.getPriceWithoutVAT())
                .comment(request.getComment())
                .status(request.getStatus().name())
                .requestRowId(request.getRequestRowId())
                .build();
    }
}