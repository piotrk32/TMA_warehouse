package com.example.tma_warehouse.models.administrator.dtos;

import com.example.tma_warehouse.models.user.enums.Status;
import lombok.Builder;

import java.time.LocalDate;

public record AdministratorResponseDTO(Long coordinatorId,
                                     Status status,
                                     String firstName,
                                     String lastName,
                                     LocalDate birthDate,
                                     String email,
                                     String phoneNumber
) {
    @Builder
    public AdministratorResponseDTO {}

}