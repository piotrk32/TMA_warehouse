package com.example.tma_warehouse.security.services;

import com.example.tma_warehouse.models.user.User;
import com.example.tma_warehouse.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FineGrainServices {
    private final UserService userService;

    public String getUserEmail() {
        return (SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public long getCurrentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUserIdByUserEmail(email);
    }

    public boolean compareSecurityEmailAndEmailByUserId(Long userId) {
        String userIdEmail = userService.getUserEmailByUserId(userId);
        return getUserEmail().equals(userIdEmail);
    }

    public boolean hasRole(String role) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
    }

    // Helper method to extract the employee's ID from the authentication object
    public Long getEmployeeIdFromAuthentication(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
    public Long getEmployeeIdFromAuthenticationForUpdate(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            return user.getId(); // Pobierz ID użytkownika z klasy User
        } else {
            throw new IllegalStateException("Szczegóły użytkownika nie są dostępne w obiekcie Authentication.");
        }
    }









}
