package com.example.tma_warehouse.controllers.administrator;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Administrator Controller", description = "Functionalities intended for administrators")
public class AdministratorController {

    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public void updateUserRoles(Long userId, List<String> roles) {
        // Method implementation
    }
}
