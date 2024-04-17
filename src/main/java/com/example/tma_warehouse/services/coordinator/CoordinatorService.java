package com.example.tma_warehouse.services.coordinator;


import com.example.tma_warehouse.exceptions.EntityNotFoundException;
import com.example.tma_warehouse.models.coordinator.Coordinator;
import com.example.tma_warehouse.models.coordinator.dtos.CoordinatorInputDTO;
import com.example.tma_warehouse.models.user.User;
import com.example.tma_warehouse.models.user.enums.Status;
import com.example.tma_warehouse.repositories.CoordinatorRepository;
import com.example.tma_warehouse.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class CoordinatorService {

    private final CoordinatorRepository coordinatorRepository;
    private final UserRepository userRepository;

    public Coordinator getCoordinatorById(Long coordinatorId) {
        return coordinatorRepository.findById(coordinatorId)
                .orElseThrow(() -> new EntityNotFoundException("Coordinator", "No coordinator found with id: " + coordinatorId));
    }

    public Coordinator createCoordinator(CoordinatorInputDTO coordinatorInputDTO, User user) {
        Coordinator coordinator = new Coordinator(
                coordinatorInputDTO.getFirstName(),
                coordinatorInputDTO.getLastName(),
                coordinatorInputDTO.getBirthDate(),
                user.getEmail(),
                coordinatorInputDTO.getPhoneNumber(),
                user.getAccessToken(),
                user.getRefreshToken(),
                user.getIdToken());
        coordinator.setRoles(Arrays.asList("ROLE_EMPLOYEE"));
        userRepository.delete(user);
        return coordinatorRepository.saveAndFlush(coordinator);
    }

    public void deleteCoordinatorById(Long coordinatorId) {
        Coordinator coordinator = getCoordinatorById(coordinatorId);
        coordinator.setStatus(Status.INACTIVE);
        coordinatorRepository.saveAndFlush(coordinator);
    }

    public Coordinator updateCoordinatorById(Long coordinatorId, CoordinatorInputDTO coordinatorInputDTO) {
        Coordinator coordinator = getCoordinatorById(coordinatorId);
        coordinator.setFirstName(coordinatorInputDTO.getFirstName());
        coordinator.setLastName(coordinatorInputDTO.getLastName());
        coordinator.setBirthDate(coordinatorInputDTO.getBirthDate());
        coordinator.setPhoneNumber(coordinatorInputDTO.getPhoneNumber());
        return coordinatorRepository.saveAndFlush(coordinator);
    }

}
