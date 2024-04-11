package com.example.tma_warehouse.models.employee.dtos;

import com.example.tma_warehouse.models.employee.Employee;

public class EmployeeMapper {

    public static EmployeeResponseDTO mapToEmployeeResponseDTO(Employee employee) {
        return EmployeeResponseDTO.builder()
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .birthDate(employee.getBirthDate())
                .email(employee.getEmail())
                .phoneNumber(employee.getPhoneNumber())
                .build();
    }

}
