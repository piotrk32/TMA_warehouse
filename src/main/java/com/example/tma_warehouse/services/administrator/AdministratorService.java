package com.example.tma_warehouse.services.administrator;

import com.example.tma_warehouse.models.administrator.Administrator;
import com.example.tma_warehouse.models.administrator.dtos.AdministratorInputDTO;
import com.example.tma_warehouse.models.user.User;
import com.example.tma_warehouse.repositories.AdministratorRepository;
import com.example.tma_warehouse.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdministratorService {
    private final UserRepository userRepository;
    private final AdministratorRepository administratorRepository;

    public Administrator createAdministrator(AdministratorInputDTO administratorInputDTO, User user) {
        Administrator administrator = new Administrator(
                administratorInputDTO.getFirstName(),
                administratorInputDTO.getLastName(),
                administratorInputDTO.getBirthDate(),
                user.getEmail(),
                administratorInputDTO.getPhoneNumber(),
                user.getAccessToken(),
                user.getRefreshToken(),
                user.getIdToken());
        userRepository.delete(user);
        return administratorRepository.saveAndFlush(administrator);
    }
}
