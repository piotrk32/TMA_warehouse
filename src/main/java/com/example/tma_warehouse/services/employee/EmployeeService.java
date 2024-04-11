package com.example.tma_warehouse.services.employee;

import com.example.tma_warehouse.models.employee.Employee;
import com.example.tma_warehouse.models.employee.dtos.EmployeeInputDTO;
import com.example.tma_warehouse.models.role.Role;
import com.example.tma_warehouse.models.user.User;
import com.example.tma_warehouse.repositories.EmployeeRepository;
import com.example.tma_warehouse.repositories.RoleRepository;
import com.example.tma_warehouse.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;

    public Employee createEmployee(EmployeeInputDTO employeeInputDTO, User user) {
        // Znajdź rolę dla pracownika, załóżmy że wszyscy pracownicy mają tą samą rolę o nazwie "ROLE_EMPLOYEE"
        Role employeeRole = roleRepository.findByName("ROLE_EMPLOYEE")
                .orElseThrow(() -> new IllegalStateException("Employee role not found"));

        Employee employee = new Employee(
                employeeInputDTO.getFirstName(),
                employeeInputDTO.getLastName(),
                employeeInputDTO.getBirthDate(),
                user.getEmail(),
                employeeInputDTO.getPhoneNumber(),
                user.getAccessToken(),
                user.getRefreshToken(),
                user.getIdToken(),
                employeeRole); // Dodajemy rolę do konstruktora

        userRepository.delete(user);
        return employeeRepository.saveAndFlush(employee);
    }
}
