package com.example.tma_warehouse.services.administrator;

import com.example.tma_warehouse.exceptions.UserAlreadyExistsException;
import com.example.tma_warehouse.models.administrator.Administrator;
import com.example.tma_warehouse.models.administrator.dtos.AdministratorInputDTO;
import com.example.tma_warehouse.models.administrator.dtos.AdministratorResponseDTO;
import com.example.tma_warehouse.models.user.User;
import com.example.tma_warehouse.models.user.enums.Status;
import com.example.tma_warehouse.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.example.tma_warehouse.models.administrator.dtos.AdministratorMapper.mapToAdministratorResponseDTO;

@Component
@RequiredArgsConstructor
public class AdministratorFacade {
    private final UserService userService;
    private final AdministratorService administratorService;


    public AdministratorResponseDTO createAdministrator(AdministratorInputDTO administratorInputDTO, String email) {
        User user = userService.getUserByEmail(email);
        if (user.getStatus() == Status.REGISTRATION_INCOMPLETE) {

            Administrator administrator = administratorService.createAdministrator(administratorInputDTO, user);

            return mapToAdministratorResponseDTO(administrator);
        }
        throw new UserAlreadyExistsException("email", "User with this email already exists.");
    }
}
