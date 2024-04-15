package com.example.tma_warehouse.models.request.dtos;

import com.example.tma_warehouse.models.request.Request;

import java.time.LocalDateTime;


public class RequestMapper {

    public static RequestResponseDTO mapToRequestResponseDTO(Request request) {
        String fullName = request.getEmployee().getFirstName() + " " + request.getEmployee().getLastName();


        return RequestResponseDTO
                .builder()
                .requestId(request.getId())
                .employeeName(fullName)
                .priceWithoutVat(request.getPriceWithoutVAT())
                .comment(request.getComment())
                .status(request.getStatus().name())
                .createdAt(LocalDateTime.parse(request.getCreatedAt().toString()))
                .modifiedAt(LocalDateTime.parse(request.getModifiedAt().toString()))
                .build();
    }


}
