package com.example.tma_warehouse.services.employee;


import com.example.tma_warehouse.exceptions.UserAlreadyExistsException;

import com.example.tma_warehouse.models.employee.Employee;
import com.example.tma_warehouse.models.employee.dtos.EmployeeInputDTO;
import com.example.tma_warehouse.models.employee.dtos.EmployeeMapper;
import com.example.tma_warehouse.models.employee.dtos.EmployeeResponseDTO;
import com.example.tma_warehouse.models.user.User;
import com.example.tma_warehouse.models.user.enums.Status;

import com.example.tma_warehouse.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.example.tma_warehouse.models.employee.dtos.EmployeeMapper.mapToEmployeeResponseDTO;


@Component
@RequiredArgsConstructor
public class EmployeeFacade {

    private final EmployeeService employeeService;
    private final UserService userService;



    public EmployeeResponseDTO createEmployee(EmployeeInputDTO employeeInputDTO, String email) {
        User user = userService.getUserByEmail(email);
        if (user.getStatus() == Status.REGISTRATION_INCOMPLETE || user.getRoles().isEmpty()) {
            Employee employee = employeeService.createEmployee(employeeInputDTO, user);
            userService.createUserWithRole(employee, Arrays.asList("ROLE_EMPLOYEE"));
            return EmployeeMapper.mapToEmployeeResponseDTO(employee);
        }
        throw new UserAlreadyExistsException("User", "User already registered.");
    }


//    public void deleteEmployeeById(Long employeeId) {
//        employeeService.deleteEmployeeById(employeeId);
//    }
//
//    public EmployeeResponseDTO updateEmployeeById(Long id, EmployeeInputDTO employeeInputDTO) {
//        Employee employee = employeeService.updateEmployeeById(id, employeeInputDTO);
//        return mapToEmployeeResponseDTO(employee);
//    }
//
//    public EmployeeResponseDTO getEmployeeById(Long employeeId) {
//        return mapToEmployeeResponseDTO(employeeService.getEmployeeById(employeeId));
//    }





}
