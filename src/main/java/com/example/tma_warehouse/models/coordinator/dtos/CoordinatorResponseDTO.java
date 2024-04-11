package com.example.tma_warehouse.models.coordinator.dtos;

import lombok.Builder;

import java.time.LocalDate;

public record CoordinatorResponseDTO(Long coordinatorId,
                                     String firstName,
                                     String lastName,
                                     LocalDate birthDate,
                                     String email,
                                     String phoneNumber,
                                     String role){

    @Builder
    public CoordinatorResponseDTO {}
}
