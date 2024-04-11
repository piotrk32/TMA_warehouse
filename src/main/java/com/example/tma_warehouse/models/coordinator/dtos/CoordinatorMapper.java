package com.example.tma_warehouse.models.coordinator.dtos;

import com.example.tma_warehouse.models.coordinator.Coordinator;
import com.example.tma_warehouse.models.employee.Employee;

public class CoordinatorMapper {

    public static CoordinatorResponseDTO mapToCoordinatorResponseDTO(Coordinator coordinator) {
        return CoordinatorResponseDTO.builder()
                .firstName(coordinator.getFirstName())
                .lastName(coordinator.getLastName())
                .birthDate(coordinator.getBirthDate())
                .email(coordinator.getEmail())
                .phoneNumber(coordinator.getPhoneNumber())
                .build();
    }

}
