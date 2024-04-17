package com.example.tma_warehouse.models.employee.dtos;

import com.example.tma_warehouse.models.employee.Employee;
import com.example.tma_warehouse.models.user.enums.Status;

import java.util.stream.Collectors;

public class EmployeeMapper {

    public static EmployeeResponseDTO mapToEmployeeResponseDTO(Employee employee) {
        return EmployeeResponseDTO.builder()
                .employeeId(employee.getId())
                .status(Status.valueOf(employee.getStatus().name()))
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .birthDate(employee.getBirthDate())
                .email(employee.getEmail())
                .phoneNumber(employee.getPhoneNumber())
                .roles(employee.getRoles()) // Assuming you have roles handled similarly
                .build();
    }
}
