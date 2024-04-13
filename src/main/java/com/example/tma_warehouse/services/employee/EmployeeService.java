package com.example.tma_warehouse.services.employee;

import com.example.tma_warehouse.exceptions.EntityNotFoundException;

import com.example.tma_warehouse.models.employee.Employee;
import com.example.tma_warehouse.models.employee.dtos.EmployeeInputDTO;
import com.example.tma_warehouse.models.user.User;
import com.example.tma_warehouse.models.user.enums.Status;
import com.example.tma_warehouse.repositories.EmployeeRepository;
import com.example.tma_warehouse.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee", "No employee found with id: " + employeeId));
    }

    public Optional<Employee> findEmployeeByEmail(String email) {
        return Optional.ofNullable(employeeRepository.findByEmail(email));
    }

    public Employee createEmployee(EmployeeInputDTO employeeInputDTO, User user) {
        Employee employee = new Employee(
                employeeInputDTO.getFirstName(),
                employeeInputDTO.getLastName(),
                employeeInputDTO.getBirthDate(),
                user.getEmail(),
                employeeInputDTO.getPhoneNumber(),
                user.getAccessToken(),
                user.getRefreshToken(),
                user.getIdToken());
        userRepository.delete(user);
        return employeeRepository.saveAndFlush(employee);
    }

    public void deleteEmployeeById(Long employeeId) {
        Employee employee = getEmployeeById(employeeId);
        employee.setStatus(Status.INACTIVE);
        employeeRepository.saveAndFlush(employee);
    }

    public Employee updateEmployeeById(Long employeeId, EmployeeInputDTO employeeInputDTO) {
        Employee employee = getEmployeeById(employeeId);
        employee.setFirstName(employeeInputDTO.getFirstName());
        employee.setLastName(employeeInputDTO.getLastName());
        employee.setBirthDate(employeeInputDTO.getBirthDate());
        employee.setPhoneNumber(employeeInputDTO.getPhoneNumber());

        return employeeRepository.saveAndFlush(employee);
    }




}






