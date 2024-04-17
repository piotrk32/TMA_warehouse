package com.example.tma_warehouse.controllers.administrator;

import com.example.tma_warehouse.models.user.User;
import com.example.tma_warehouse.models.user.dtos.UserRolesUpdateDTO;
import com.example.tma_warehouse.services.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/administrator")
@RequiredArgsConstructor
@Tag(name = "Administrator Controller", description = "Functionalities intended for administrators")
public class AdministratorController {

    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(AdministratorController.class);





    @PostMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<?> updateUserRoles(@PathVariable Long userId, @RequestBody UserRolesUpdateDTO rolesUpdateDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.debug("Current user: " + auth.getName() + ", Roles: " + auth.getAuthorities());

        User updatedUser = userService.updateUserRoles(userId, rolesUpdateDTO.getRoles());

        return ResponseEntity.ok("User roles updated successfully for " + updatedUser.getEmail());
    }
}
