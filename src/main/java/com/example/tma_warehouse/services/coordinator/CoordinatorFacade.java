package com.example.tma_warehouse.services.coordinator;

import com.example.tma_warehouse.exceptions.UserAlreadyExistsException;


import com.example.tma_warehouse.models.coordinator.Coordinator;
import com.example.tma_warehouse.models.coordinator.dtos.CoordinatorInputDTO;
import com.example.tma_warehouse.models.coordinator.dtos.CoordinatorMapper;
import com.example.tma_warehouse.models.coordinator.dtos.CoordinatorResponseDTO;
import com.example.tma_warehouse.models.employee.Employee;
import com.example.tma_warehouse.models.employee.dtos.EmployeeInputDTO;
import com.example.tma_warehouse.models.employee.dtos.EmployeeMapper;
import com.example.tma_warehouse.models.employee.dtos.EmployeeResponseDTO;
import com.example.tma_warehouse.models.user.User;
import com.example.tma_warehouse.models.user.enums.Status;

import com.example.tma_warehouse.services.employee.EmployeeService;
import com.example.tma_warehouse.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.example.tma_warehouse.models.coordinator.dtos.CoordinatorMapper.mapToCoordinatorResponseDTO;


@Component
@RequiredArgsConstructor
public class CoordinatorFacade {

    private final UserService userService;

    private final CoordinatorService coordinatorService;
    private final EmployeeService employeeService;


    public CoordinatorResponseDTO createCoordinator(CoordinatorInputDTO coordinatorInputDTO, String email) {
        User user = userService.getUserByEmail(email);
        if (user.getStatus() == Status.REGISTRATION_INCOMPLETE || user.getRoles().isEmpty()) {
            Coordinator coordinator = coordinatorService.createCoordinator(coordinatorInputDTO, user);
            userService.createUserWithRole(coordinator, Arrays.asList("ROLE_COORDINATOR"));
            return CoordinatorMapper.mapToCoordinatorResponseDTO(coordinator);
        }
        throw new UserAlreadyExistsException("User", "User already registered.");
    }

    public void deleteCoordinatorById(Long coordinatorId) {
        coordinatorService.deleteCoordinatorById(coordinatorId);
    }

    public CoordinatorResponseDTO updateCoordinatorById(Long id, CoordinatorInputDTO coordinatorInputDTO) {
        Coordinator coordinator = coordinatorService.updateCoordinatorById(id, coordinatorInputDTO);
        return mapToCoordinatorResponseDTO(coordinator);
    }

    public CoordinatorResponseDTO getCoordinatorById(Long coordinatorId) {
        return mapToCoordinatorResponseDTO(coordinatorService.getCoordinatorById(coordinatorId));
    }


}
