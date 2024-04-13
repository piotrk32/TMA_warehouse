package com.example.tma_warehouse.models.coordinator.dtos;

import com.example.tma_warehouse.models.coordinator.Coordinator;
import com.example.tma_warehouse.models.user.enums.Status;

public class CoordinatorMapper {

    public static CoordinatorResponseDTO mapToCoordinatorResponseDTO(Coordinator coordinator) {
        return CoordinatorResponseDTO.builder()
                .coordinatorId(coordinator.getId())
                .status(Status.valueOf(coordinator.getStatus().name()))
                .firstName(coordinator.getFirstName())
                .lastName(coordinator.getLastName())
                .birthDate(coordinator.getBirthDate())
                .email(coordinator.getEmail())
                .phoneNumber(coordinator.getPhoneNumber())
                .build();
    }

}
