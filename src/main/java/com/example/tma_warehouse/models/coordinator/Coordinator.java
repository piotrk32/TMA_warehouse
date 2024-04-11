package com.example.tma_warehouse.models.coordinator;

import com.example.tma_warehouse.models.role.Role;
import com.example.tma_warehouse.models.user.User;

import java.time.LocalDate;

public class Coordinator extends User {

    public Coordinator(String firstName,
                    String lastName,
                    LocalDate birthDate,
                    String email,
                    String phoneNumber,
                    String accessToken,
                    String refreshToken,
                    String idToken,
                    Role role) {
        super(firstName, lastName, birthDate, email, phoneNumber, accessToken, refreshToken, idToken, role);
    }
}
