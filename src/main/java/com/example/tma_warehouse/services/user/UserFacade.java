package com.example.tma_warehouse.services.user;


import com.example.tma_warehouse.models.user.dtos.UserResponseDTO;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;

    public boolean userExists(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getClaim("email");
        return userService.userExistsByEmail(email);
    }
    public void deleteUserById(Long userId) {
        userService.deleteUserById(userId);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }



}
