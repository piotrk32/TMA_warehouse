package com.example.tma_warehouse.models.employee.dtos;

import com.example.tma_warehouse.models.employee.Employee;
import com.example.tma_warehouse.models.role.Role;
import lombok.Builder;

import java.time.LocalDate;

public record EmployeeResponseDTO (Long employeeId,
    String firstName,
    String lastName,
    LocalDate birthDate,
    String email,
    String phoneNumber,
    String role){

    @Builder
    public EmployeeResponseDTO{}
}
