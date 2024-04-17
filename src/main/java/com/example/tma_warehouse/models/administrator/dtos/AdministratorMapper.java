package com.example.tma_warehouse.models.administrator.dtos;

import com.example.tma_warehouse.models.administrator.Administrator;
import com.example.tma_warehouse.models.user.enums.Status;

public class AdministratorMapper {

    public static AdministratorResponseDTO mapToAdministratorResponseDTO(Administrator administrator) {
        return AdministratorResponseDTO.builder()
                .coordinatorId(administrator.getId())
                .status(Status.valueOf(administrator.getStatus().name()))
                .firstName(administrator.getFirstName())
                .lastName(administrator.getLastName())
                .birthDate(administrator.getBirthDate())
                .email(administrator.getEmail())
                .phoneNumber(administrator.getPhoneNumber())
                .build();
    }

}
