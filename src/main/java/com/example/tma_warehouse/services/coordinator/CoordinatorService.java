package com.example.tma_warehouse.services.coordinator;

import com.example.tma_warehouse.models.coordinator.Coordinator;
import com.example.tma_warehouse.models.coordinator.dtos.CoordinatorInputDTO;
import com.example.tma_warehouse.models.employee.Employee;
import com.example.tma_warehouse.models.role.Role;
import com.example.tma_warehouse.models.user.User;
import com.example.tma_warehouse.repositories.CoordinatorRepository;
import com.example.tma_warehouse.repositories.RoleRepository;
import com.example.tma_warehouse.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoordinatorService {

    private final UserRepository userRepository;
    private final CoordinatorRepository coordinatorRepository;
    private final RoleRepository roleRepository;



    public Coordinator createCoordinator(CoordinatorInputDTO coordinatorInputDTO, User user) {
        // Znajdź rolę dla pracownika, załóżmy że wszyscy pracownicy mają tą samą rolę o nazwie "ROLE_EMPLOYEE"
        Role coordinatorRole = roleRepository.findByName("ROLE_EMPLOYEE")
                .orElseThrow(() -> new IllegalStateException("Employee role not found"));

        Coordinator coordinator = new Coordinator(
                coordinatorInputDTO.getFirstName(),
                coordinatorInputDTO.getLastName(),
                coordinatorInputDTO.getBirthDate(),
                user.getEmail(),
                coordinatorInputDTO.getPhoneNumber(),
                user.getAccessToken(),
                user.getRefreshToken(),
                user.getIdToken(),
                coordinatorRole); // Dodajemy rolę do konstruktora

        userRepository.delete(user);
        return coordinatorRepository.saveAndFlush(coordinator);
    }
}
