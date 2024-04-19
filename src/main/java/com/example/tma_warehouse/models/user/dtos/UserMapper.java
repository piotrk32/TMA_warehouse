package com.example.tma_warehouse.models.user.dtos;

import com.example.tma_warehouse.models.employee.Employee;
import com.example.tma_warehouse.models.employee.dtos.EmployeeResponseDTO;
import com.example.tma_warehouse.models.user.User;
import com.example.tma_warehouse.models.user.enums.Status;

public class UserMapper {

    public static UserResponseDTO mapToUserReposonseDTO(User user) {
        return UserResponseDTO.builder()
                .userId(user.getId())
                .status(Status.valueOf(user.getStatus().name()))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .roles(user.getRoles())
                .build();
    }
}
