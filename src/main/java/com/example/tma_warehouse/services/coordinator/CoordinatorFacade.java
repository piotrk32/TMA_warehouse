package com.example.tma_warehouse.services.coordinator;

import com.example.tma_warehouse.exceptions.UserAlreadyExistsException;
import com.example.tma_warehouse.models.coordinator.dtos.CoordinatorInputDTO;
import com.example.tma_warehouse.models.coordinator.dtos.CoordinatorResponseDTO;
import com.example.tma_warehouse.models.employee.dtos.EmployeeInputDTO;
import com.example.tma_warehouse.models.employee.dtos.EmployeeResponseDTO;
import com.example.tma_warehouse.models.user.User;
import com.example.tma_warehouse.models.user.enums.Status;
import com.example.tma_warehouse.services.employee.EmployeeService;
import com.example.tma_warehouse.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.example.tma_warehouse.models.coordinator.dtos.CoordinatorMapper.mapToCoordinatorResponseDTO;
import static com.example.tma_warehouse.models.employee.dtos.EmployeeMapper.mapToEmployeeResponseDTO;

@Component
@RequiredArgsConstructor
public class CoordinatorFacade {

    private final CoordinatorService coordinatorService;
    private final UserService userService;


    public CoordinatorResponseDTO createCoordinator(CoordinatorInputDTO coordinatorInputDTO, String email) {
        User user = userService.getUserByEmail(email);
        if (user.getStatus() == Status.REGISTRATION_INCOMPLETE) {
            return mapToCoordinatorResponseDTO(coordinatorService.createCoordinator(coordinatorInputDTO, user));
        }
        throw new UserAlreadyExistsException("User", "User already registered.");
    }
}
